# Intellij Object Calisthenics Analyzer Plugin
An IntelliJ plugin for analyzing your Java source code's adherence to [Object Calisthenics](http://www.cs.helsinki.fi/u/luontola/tdd-2009/ext/ObjectCalisthenics.pdf).

You can add this plugin in IntelliJ through File->Settings...->Plugins->Browse repositories... (search for object calisthenics). Or you can [download it here](https://github.com/chairbender/intellij-object-calisthenics-analyzer/releases/download/0.82/intellij-object-calisthenics-analyzer-0.82.zip). Install it in IntelliJ by going to File->Settings...->Plugins->Install plugin from disk... Once installed, you should see an additional menu item called Object Calisthenics. Click the "Analyze" action within that menu to generate a report covering all of the Java code in your project.

Do you want to get better at Object Oriented programming?

If you want to get better at something, you should practice. By attempting to write code that conforms to the 9 rules laid out by Object Calisthenics, you can force yourself to overcome your usual development habits and challenge yourself to make use of the full spectrum of OOP tools, thereby improving your familiarity with the designs afforded by OOP. With Object Calisthenics, you'll have no more 1000+ line "Util" classes, no more "do everything" objects, and certainly no more actually getting things finished in a reasonable amount of time - just clean, readable code that's easy to maintain and robust against change.  

This plugin makes this process as easy as clicking a button. With it, you can quickly locate rule violations so you can focus on what matters: improving your design, iteration by iteration. It's also brutally honest, so you'll never cheat yourself out of a possible learning experience. It even tries to offer you some quick tips for handling each type of rule violation, summarizing the points in the original Object Calisthenics paper!

Makes use of my [Object Calisthenics Analyzer API](https://github.com/chairbender/object-calisthenics-analyzer). Supports checking for pretty much all 9 calisthenics rules (with a few small exceptions).

Please let me know of any issues or requests using the "Issues" feature of this repository!
