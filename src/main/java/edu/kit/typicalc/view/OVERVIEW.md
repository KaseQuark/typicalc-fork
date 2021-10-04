# Basic structure of the main application screen (TypeInferenceView, layout: MainViewImpl)

```
+--------------------------------------------------------------------------+
| UpperBar: language select, InputBar, help button                         |
| - InputBar: syntax button, input fields, character/example/submit button |
+--------------------------------------------------------------------------+
|                                                                          |
| left, hidden by default:  middle:             right, visible by default: |
| TypeInferenceRules        MathjaxUnification  MathjaxExplanation         |
|                           MathjaxProofTree                               |
|                                                                          |
+--------------------------------------------------------------------------+
| ControlPanel: share button, first/previous/next/last buttons             |
+--------------------------------------------------------------------------+
```

## Non-obvious architecture decisions

### Showing the proof tree step-by-step

In `mathjax-proof-tree.ts` the fully rendered MathJax output is analyzed and grouped into pieces.
Styles are applied such that only the final conclusion is initially visible.
When navigating to another step, the respective elements are styled to be visible/hidden.
Note: MathJax is only involved when initially rendering the full tree!

### Highlighting type variables + Tooltips on steps

See `mathjax-style-hacks.ts` for a detailed description of the first stage: capturing hover events (= the user moved
the mouse cursor over a relevant element). Note that the same method is used in `mathjax-proof-tree.ts` and
`mathjax-unification.ts` (where the event is just forwarded to the proof tree element).
If a type variable is targeted, a custom style definition is created that colors all instances of this variable
(see `mathjax-proof-tree.ts`, function `handleMouseEvent` for details). It is applied to both the proof tree element
and the unification element.
If a step label is targeted, a tooltip is rendered next to it (was very annoying to fine-tune).

### Different LaTeX created for MathJax / share dialog

The last paragraph requires that some elements of the proof tree / unification steps are marked using HTML classes
and CSS IDs. It would be useless to expose this implementation detail to end users (especially since the relevant
LaTeX macros are unlikely to be defined in the user's environment).
Note that the actual content is the same for both "modes".