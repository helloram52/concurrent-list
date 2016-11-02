import re, json

filename = "/Users/vads/Documents/Semester III/Intro To Multicore/Projects/ConcurrentLinkedLists/crap.log"
# Extract thread id, start/end of an operation, operation itself
threadSize = 4
result = { str(x): [] for x in range(1, threadSize + 1)}
"""
LEt's hsave the below structure
{
  "thread1" : [
    { range: [start, end], operation: "search(5): false"}
  ],
  ....
}
"""
content = []
with open(filename) as f:
  content = f.readlines()

lineNumber = 1
for x in content:
  text = re.sub(r'.* INFO : ', r'', x)
  threadName, operationID, operation = "", 0, ""

  if 'Invoking' in text:

    m = re.search(r'pool-\d+-thread-(\d+) : Invoking OperationID: (\d+)', text)
    threadName, operationID = m.group(1), m.group(2)
    #print "threadName: {}, operationid: {}".format(threadName, operationID)
    newHash = {
      "range" : [lineNumber],
      "operation" : ""
    }
    result[threadName].append(newHash)
  else:

    m = re.search(r'pool-\d+-thread-(\d+) : OperationID: (\d+) (.*)$', text)
    threadName, operationID, operation = m.group(1), m.group(2), m.group(3)
    #print "threadName: {}, operationid: {}, operation: {}".format(threadName, operationID, operation)
    result[threadName][-1]["range"].append(lineNumber)
    result[threadName][-1]["operation"] = operation

  lineNumber += 1

print json.dumps(result, sort_keys=True, indent=2)
