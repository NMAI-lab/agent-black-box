# Agent Black Box

Notes for the discussion

- What is the ideal debugger/ black box for an agent?
  - What was in the belief base?
  - What were the available plans?
  - What event triggered and why?
  - What were the options? What intention was selected? Why?
  - Link from environment, through perceptions, to reasoning state/ state of mind, to action, to effect on the environment
- Tools available today:
  - Mind inspector
  - Logging perceptions, actions, messages, etc
  - Adding diagnostic messages automatically (possible without much implementation effort)
- What is needed?
  - Ability for a human to cut through large swaths of irrelevant data to get to the interesting stuff/
  - Visually representing the snapshots in a useful way
 
Notes from the discussion
- Look at EMAS for what is out there
  - Why did I do that button
- EXTRAAMAS - all about explanations
  - Doubtful that anyone has tied explanations with BDI
  - What do people mean by explanation for agents?
- Attend as many AAMAS sessions (EXTRAAMAS, EMAS, etc) as I can
- RabbitMQ system for logging - Pub and Sub - connect to a port
  - Launch it as a service, don't need to mess with sockets
  - https://www.rabbitmq.com/
- MAS Competition papers - mention debuggability. Who are they citing?


## MAPC (Competition) Issues
- MAPC Publications: https://multiagentcontest.org/publications/

1. **Async Simulation:** When execution is paused (i.e., inspector) the agents pause but the simulation continues
2. **Performance Issues:** The Jason mind inspector is slow
    - Stores snapshot at every cycle
        - Instead, use diff updates
    - When there are a lot of cycles in the inspector, it starts to freeze
        - Hard and slow to sift through, even when throwing more memory at it (no CPU bottleneck either). Maybe a software lock bottleneck?
3. **No Support for Custom Data Structures**
    - I.e., if I have a map stored in a graph, there is no way to visualize (or even show) the data in the mind inspector
4. **Logging is either too sparse or way too verbose**
    - Changing the log level to something more verbose than 'info' is way too verbose..

## Nice-To-Haves

- Jason breakpoints
    - 'debug' breakpoints exist in Jason, but they ONLY get executed if you are in the mind inspector's debugger (i.e. slow and makes it difficult to handle large amounts of cycles....)
    - How to handle breakpoints when multi-threaded or multi-agent?
    - How to pause simulation when breakpoint is hit?
        - Maybe some sort of debugging interface/API that the simulation can connect to and be controlled?

### Data Analytics + Components

- This is where we approach the field of big data / analytics / telemetry
- **Black Box:**  Connect to an async message queue (MQ) for agent state and general logging
    - Everything sent to the MQ is a JSON object (even agent state)
        - This allows the data to be processed and consumed in different ways (logger, visualizer, etc.)
    - Introduce hooks into Jason to monitor inputs, outputs, and various metrics
        - Send events to MQ for component input, output, and metrics
        - Tag these events with timestamp, Jason cycle, etc.
    - Support for everything: even custom defined objects from agent devs
        - They can register their components and send their own custom events

- **API and/or Front End:** Consume from MQ
    - Have default endpoints that allow data to be visualized
        1. Mind inspector v3000 that re-constructs state at cycle x by building up individual messages (think git diffs)
        2. Log filter: better filtering of log output
        3. Powerful querying (e.g. 'get me agent state at cycle 220', or 'get all cycles >= 5 where +!test is one of many applicable plans', or 'get all cycles and plans that unify a context with belief x(...)'
            - This could potentially be very useful for debugging
            - Since everything can be recorded, there is the potential to 'replay' an agent using the logged inputs from the events. This can be amazing especially if we provide an interface for environment simulators to connect to.
        4. Developers can register custom end points that allow them to consume their own custom logged data 
            - (e.g. my competition map visualizer would be a 'plugin' to our system and consume my custom map data structure)
