package mp.org.blip.service;

import lombok.Data;
import lombok.Getter;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
class CycleInfo{
    private Boolean found = false;
    private String cycleNode1 = "";
    private String cycleNode2 = "";
}

@Component
public class DAGValidationService {

    private void dfs(String node, Map<String, Set<String>> graph, Map<String, Integer> nodeState, CycleInfo info){
        // mark as processing
        nodeState.put(node, 1);
        for (String child: graph.get(node)) {
            // get state of child
            Integer state = nodeState.get(child);
            if(state == 0){
                // do dfs from here
                dfs(child, graph, nodeState, info);
                // if cycle is found during this expedition then return
                if(info.getFound())
                    return;
            }
            if(state == 1){
                // cycle nodes found
                info.setFound(true);
                info.setCycleNode1(node);
                info.setCycleNode2(child);
                return;
            }
        }
        nodeState.put(node, 2);
    }

    private void findCircularDependency(ValidationContext validationContext, Map<String, Set<String>> graph){
        Map<String, Set<String>> taskDependenciesMapping = validationContext.getTaskDependenciesMapping();
        List<String> stack = new Stack<>();
        Map<String, Integer> nodeState = new HashMap<>();
        taskDependenciesMapping.forEach((task, dependencies) -> {
            if(dependencies.isEmpty())
                stack.add(task);
            nodeState.put(task, 0);
        });
        // if no independent nodes then put the first one in stack
        if(stack.isEmpty()){
            stack.add(validationContext.getJobDefinition().getTasks().getFirst().getId());
        }
        CycleInfo info = new CycleInfo();
        for(String node : stack){
            dfs(node, graph, nodeState, info);
            if(info.getFound())
                break;
        }
        if(info.getFound()){
            Map<String, Integer> taskPositions = new HashMap<>();
            for(int i = 0;i< validationContext.getJobDefinition().getTasks().size();i++){
                taskPositions.put(validationContext.getJobDefinition().getTasks().get(i).getId(), i);
            }
            String cycleNode1 = info.getCycleNode1();
            String cycleNode2 = info.getCycleNode2();

            int pos1 = taskPositions.get(cycleNode1);
            int pos2 = taskPositions.get(cycleNode2);
            validationContext.addError(new ValidationError("tasks", "Circular dependency detected at tasks["+pos1+"] and tasks["+pos2+"]."));
        }
    }

    // go through dependencies and create a dag
    public void validate(ValidationContext validationContext) {
        Map<String, Set<String>> taskDependenciesMapping = validationContext.getTaskDependenciesMapping();
        Map<String, Integer> taskIndegreeCount = new HashMap<>();
        Map<String, Set<String>> graph = new HashMap<>();
        // populating the required
        taskDependenciesMapping.forEach((task, dependencies) ->{
            graph.computeIfAbsent(task, k -> new HashSet<>());
            taskIndegreeCount.put(task, dependencies.size());
            dependencies.forEach(d ->{
                // d -> task
                graph.computeIfAbsent(d, k -> new HashSet<>()).add(task);
            });
        });

        // initialize queue and counts
        int totalNodes = taskDependenciesMapping.size();
        int countedNodes = 0;
        Queue<String> toBeProcessed = new LinkedList<>();
        taskIndegreeCount.forEach((task, count) -> {
            if(count == 0)
                toBeProcessed.add(task);
        });

        // start bfs
        while(!toBeProcessed.isEmpty()){
            String currentTask = toBeProcessed.poll();
            countedNodes += 1;
            // go through each child
            graph.get(currentTask).forEach(child -> {
                taskIndegreeCount.put(child, taskIndegreeCount.get(child) - 1);
                if(taskIndegreeCount.get(child) == 0)
                    toBeProcessed.add(child);
            });
        }

        // check if dag has cycle or not
        if(countedNodes < totalNodes){
            this.findCircularDependency(validationContext, graph);
        }
    }
}
