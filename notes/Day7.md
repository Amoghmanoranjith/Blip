## DAG validation
- I will be using Kahn's algorithm for this
- to put this simply we start by queuing nodes whose in degree is 0 we process a node like this and reduce the in degree of its children by 1
- for any child in question if its indegree becomes 0 we put in queue
- first i need a data structure which allows me to go from a node to its children
- another array to keep track of in degree of each node

## Cycle location
- We use dfs with 3 states here
- 0 - unvisited
- 1 - visiting
- 2 - visited
- Take all independent nodes and process each as below
- Start dfs from a node 
- go to each child and dfs from here
- if node has no children change state to 2 and return
- while processing a node we will come across its children if child is 1 then node and child are involved in a cycle report them
- if child state is 2 dont put it up for processing