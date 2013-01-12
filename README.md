cljs-painkiller
===============

Sometimes you write JavaScript like this:

```javascript
var Bag = function() {
  this.store = [];
}

Bag.prototype.add = function(val) {
  this.store.push(val);
}

Bag.prototype.print = function(val) {
  console.log(this.store);
}

var mybag = new Bag();
mybag.add(5);
mybag.add(7);
mybag.print();
```

Sometimes you have to do it in ClojureScript, like when talking to some JS libraries. Or your love for objects is so strong that you venture off the True Path and just want to do it.

Normally ClojureScript would make it look like this:

```clojure
(defn Bag []
  (this-as this
           (set! (.-store this) (array))
           this))

(set! (.. Bag -prototype -add)
      (fn [val]
        (this-as this
                 (.push (.-store this) val))))

(set! (.. Bag -prototype -print)
      (fn []
        (this-as this
                 (.log js/console (.-store this)))))

(def mybag (Bag.))
(.add mybag 5)
(.add mybag 7)
(.print mybag)
```

Hideous!

Take this little painkiller pill here and it will look more like:

```clojure
(defn Bag []
  (this-as this
           (set! (.-store this) (array))
           this))

(set-obj-fn Bag.prototype.add [val]
            (.push (.-store this) val))

(set-obj-fn Bag.prototype.print [val]
            (.log js/console (.-store this)))
```

There are two macros in the package. `set-obj-fn` shown above, which binds the function to path on object that looks more like JS.

There also is `obj-fn`, which only eliminates the need for explicit `this-as`:
```clojure
(set! (.. Bag -prototype -add)
      (obj-fn [val]
        (.push (.-store this) val)))
```

All you need is:
```clojure
; project.clj
[cljs-painkiller "0.1.0"]
```

```clojure
; my-namespace.cljs:
(:use-macros [painkiller.macros :only [obj-fn set-obj-fn]])
```
