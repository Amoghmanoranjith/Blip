## The task execution graph needs some clarifications:

Can the graph have multiple beginnings?
- Yes, we want to run them async

Can a node have multiple inputs?
- Yes

Can a node have multiple outputs?
- Yes

What does a dependency mean?
- It means this particular node has to execute after all its dependencies are executed

What does output mean?
- If a task is able to produce any result then we name this result with a variable name in our case it is called output.

Is the defined dependency the only dependency in reality?
- No, in the parameters when we use an output it also becomes a dependency.
- In this case if a task is using an output then the task generating the output also has to exec before this task. Thus a soft dependency.
- In case user attempts to use an output that will be populated after this task then this is an exception
- We have two options here:
  - Allow user to do so, we just use null value here and the execution fails during run time (transferring headache to my future self)
  - Create two graphs one based on dependencies, second based on parameters which is not a big deal actually, a second dependency list is created this will hold the tasks whose output is used in this task. (Can add this later in SpecificValidator.java)

## Concerns regarding execution:

Lets say i have two flows:
1. a → b
2. c → d → b

During execution a might complete first and d might take some time to execute this means b has to be in a waiting state so this something to ponder about later

### Http method validations
- validate for method type
- extract the variables and check if they are present or not among all tasks not the fields only the output names
- if it is get method then it has to have a body