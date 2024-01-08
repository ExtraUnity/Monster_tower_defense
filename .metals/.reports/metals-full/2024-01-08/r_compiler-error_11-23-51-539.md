file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/App.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

action parameters:
offset: 44
uri: file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/App.java
text:
```scala
package dk.dtu.mtd;

import dk.dtu.mtd.con@@troller.Controller;
import dk.dtu.mtd.view.Gui;

public class App {

    public static void main(String[] args) {
        Gui.initGui();
        Controller.initController();
    }
   
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:933)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:168)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.HoverProvider$.hover(HoverProvider.scala:34)
	scala.meta.internal.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:342)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator