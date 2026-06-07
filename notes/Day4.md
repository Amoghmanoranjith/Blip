Took a break for 2 days, stupid college work took all the time
So where was i at?
I am done with Yaml Parse Time exceptions
Moving on to generic structural validation
Okay so i throw a Schema Exception for this with all the validation errors
Resolve it in executionExceptionHandler

Going ahead with Semantic Validation
Done

Going ahead with specific validation this will be controlled by SpecificValidator.java

The question is will the Constraint violations hold the property name now also
when i convert from Generic Definition to specific pojos

for each definition i have a parent validator that resolves the given type and calls the required sub validator

![img.png](img.png)

it ends up looking like this, the point is to make this extensible,
lets say we add a new task now we need to update task types enum, and the task validator parent

Http Validator:
let us define the 