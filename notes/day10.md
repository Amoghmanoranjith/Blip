- Grasping how reactive works i learnt that blocking work can still be made reactive
- But the blocking threads are still blocked, the threads that dont get blocked are netty threads
- Netty threads will delegate the blocking task to a Scheduler
- In this case that will be boundedElastic thread pool, these threads are maintained to be limited and can be blocking

Today we move to the execution engine