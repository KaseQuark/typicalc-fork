import {MathjaxAdapter} from "./mathjax-adapter";
import {hoverAreaAroundElements} from "./mathjax-style-hacks";

declare let window: {
    svgPanZoomFun: (svg: SVGElement, options: {
        fit: boolean;
        controlIconsEnabled: boolean;
        customEventsHandler: {
            init: (options: any) => void;
            haltEventListeners: string[];
            destroy: () => void
        };
    }) => void;
}
// these attributes and functions are supported by major browsers, but TS does not know about them
declare global {
    interface SVGElement {
        getElementById: (id: string) => SVGGraphicsElement | null;
        viewBox: SVGAnimatedRect;
    }

    interface SVGGraphicsElement {
        getTransformToElement: (other: SVGGraphicsElement) => SVGMatrix;
    }

    interface SVGTransformList {
        [index: number]: SVGTransform;
    }
}

interface ProofTreeServer {
    setStepCount: (count: number) => void;
}

class MathjaxProofTree extends MathjaxAdapter {
    private steps: [SVGElement, SVGElement[]][] = [];
    private $server: ProofTreeServer | undefined;

    protected showStep(n: number): void {
        for (let current = 0; current < this.steps.length; current++) {
            this.steps[current][0].style.display = "none";
            for (const node of this.steps[current][1]) {
                node.style.display = "none";
            }
        }
        for (let current = 0; current < this.steps.length && current <= n; current++) {
            this.steps[current][0].style.display = "";
            for (const node of this.steps[current][1]) {
                node.style.display = "";
            }
        }
    }

    protected calculateSteps(extraData: any): void {
        const data = typeof extraData === "string" ? JSON.parse(extraData) : [];
        const root = this.shadowRoot!;
        // setup style container for styles applied on hover
        let hoverStyles = root.querySelector("#typicalc-hover-styles");
        if (!hoverStyles) {
            hoverStyles = document.createElement('style');
            hoverStyles.id = "typicalc-hover-styles";
            root.querySelector("mjx-head")!.appendChild(hoverStyles);
        }
        const unificationEl = root.host.parentElement!.parentElement!.querySelector("tc-unification")!;
        let hoverStylesUnification = unificationEl.shadowRoot!.querySelector("#typicalc-hover-styles");
        if (!hoverStylesUnification) {
            hoverStylesUnification = document.createElement('style');
            hoverStylesUnification.id = "typicalc-hover-styles";
            unificationEl.shadowRoot!.querySelector("mjx-head")!.appendChild(hoverStylesUnification);
        }
        // set the size of the rendered SVG to the size of the container element
        if (!root.querySelector("#style-fixes")) {
            const style = document.createElement('style');
            style.innerHTML = "\
                        mjx-doc, mjx-body, mjx-container, #tc-content, svg {\
                            height: 100%;\
                        }\
                        mjx-container {\
                            margin: 0 !important;\
                        }\
                        #typicalc-definition-abs, #typicalc-definition-abs-let, #typicalc-definition-app,\
                        #typicalc-definition-const, #typicalc-definition-var, #typicalc-definition-var-let, #typicalc-definition-let {\
                            display: none;\
                            border: 2px solid red;\
                        }" + hoverAreaAroundElements;
            style.innerHTML += "svg { width: 100%; }";
            style.id = "style-fixes";
            root.querySelector("mjx-head")!.appendChild(style);
        }

        const semanticsMatch = (semantics: string) => semantics.indexOf("bspr_inference:") >= 0;
        const inferenceRuleSelector = 'g[semantics="bspr_inferenceRule:down"]';
        const labelSelector = 'g[semantics="bspr_prooflabel:left"]';
        const stepSelector = 'g[typicalc="step"]';
        // space between inference premises
        const padding = 300;
        console.log("calculating steps..");

        if (this.shadowRoot !== null) {
            console.time('stepCalculation');
            const svg = this.shadowRoot.querySelector<SVGElement>("svg")!;
            let root = this.shadowRoot.querySelector("#typicalc-prooftree")! as SVGElement;
            while (!root.getAttribute("semantics")) {
                root = root.parentNode! as SVGElement;
            }
            // first, enumerate all of the steps
            let stepIdx = 0;
            for (const a of [root, ...root.querySelectorAll<SVGElement>("g[semantics]")]) {
                let semantics = a.getAttribute("semantics")!;
                if (semanticsMatch(semantics)) {
                    a.setAttribute("typicalc", "step");
                    a.setAttribute("id", "step" + stepIdx);
                    stepIdx++;
                }
            }
            // then create the steps
            let steps: [SVGElement, SVGElement[]][] = [];
            stepIdx = 0;
            for (const a of [root, ...root.querySelectorAll<SVGElement>("g[semantics]")]) {
                let semantics = a.getAttribute("semantics")!;
                if (semanticsMatch(semantics)) {
                    const id = "step" + stepIdx;
                    const idSelector = `#${id} `;
                    stepIdx++;

                    // find the next one/two steps above this one
                    const aboveStep1 = a.querySelector<SVGElement>(idSelector + stepSelector);
                    let above = [];
                    if (aboveStep1 != null) {
                        const parent = aboveStep1.parentElement!.parentElement!;
                        parent.setAttribute("id", "typicalc-selector");
                        for (const node of parent.querySelectorAll<SVGElement>('#typicalc-selector > g > ' + stepSelector)) {
                            above.push(node);
                        }
                        parent.removeAttribute("id");
                    }
                    const rule = a.querySelector(idSelector + inferenceRuleSelector);
                    if (rule !== null) {
                        let i = 0;
                        for (const node of rule.children) {
                            if (i !== 1) {
                                above.push(node as SVGElement);
                            }
                            i += 1;
                        }
                    }
                    const label = a.querySelector<SVGElement>(idSelector + labelSelector);
                    if (label) {
                        above.push(label);
                    }
                    if (stepIdx === 1) {
                        // initial step should not show premises
                        steps.push([a, []]);
                    }
                    steps.push([a, above]);
                }
            }
            // limit start zoom
            svg.viewBox.baseVal.width = Math.min(100000, svg.viewBox.baseVal.width);
            svg.viewBox.baseVal.width = Math.max(20000, svg.viewBox.baseVal.width);

            // MathJax layout of bussproofs is sometimes wrong:
            // https://github.com/mathjax/MathJax/issues/2270
            // https://github.com/mathjax/MathJax/issues/2626
            // we fix it in two phases (executed at the same time):
            // 1. fix overlapping by iterating over "rows" in the SVG created by MathJax
            // in each row, the elements are arranged to not overlap
            // 2. place inference conclusions under the center of their line
            const nodeIterator = [...root.querySelectorAll<SVGGraphicsElement>("g[data-mml-node='mtr']," + inferenceRuleSelector)];
            console.log(`working with ${nodeIterator.length} nodes`);
            // start layout fixes in the innermost part of the SVG
            nodeIterator.reverse();
            for (const row of nodeIterator) {
                const semantics = row.getAttribute("semantics");
                if (semantics === "bspr_inferenceRule:down") {
                    const conclusion = row.children[1].querySelector<SVGGraphicsElement>('g[data-mml-node="mstyle"]')!;
                    const conclusionBox = conclusion.getBBox();
                    const line = row.children[2] as SVGGraphicsElement;
                    const bbox = line.getBBox();

                    const offset2 = line.getTransformToElement(conclusion);
                    let dx = bbox.width / 2 + offset2.e - conclusionBox.width / 2;
                    dx += Number(line.getAttribute("x1"));
                    let table = row.parentNode as SVGGraphicsElement;
                    let prevNode;
                    let magicNumber = 0;
                    while (table.getAttribute("semantics") !== "bspr_inferenceRule:down") {
                        prevNode = table;
                        table = table.parentNode as SVGGraphicsElement;
                        if (table.getAttribute("data-mml-node") === "mtr" && table.childNodes.length === 3) {
                            for (let i = 0; i < table.childNodes.length; i++) {
                                if (table.childNodes[i] === prevNode) {
                                    magicNumber = i;
                                }
                            }
                        }
                        if (table === svg) {
                            break;
                        }
                    }
                    conclusion.transform.baseVal[0].matrix.e += dx;
                    if (magicNumber !== 0) {
                        continue;
                    }
                    if (table === svg) {
                        break; // last step
                    }
                    const lineBelow = table.children[2] as SVGGraphicsElement;
                    let lineStart = 0;
                    if (lineBelow) {
                        const offset1 = lineBelow.getTransformToElement(conclusion);
                        lineStart = -offset1.e;
                        const x1 = Number(lineBelow.getAttribute("x1"));
                        const x2 = Number(lineBelow.getAttribute("x2"));
                        lineBelow.setAttribute("x1", String(x1 + lineStart));
                        lineBelow.setAttribute("x2", String(x2 + lineStart));
                    }
                    const label = table.parentElement!.children[1] as SVGGraphicsElement;
                    if (label && label.transform) {
                        label.transform.baseVal[0].matrix.e += lineStart;
                    }
                } else {
                    let left = null;
                    let i = 0;
                    for (const rawNode of row.children) {
                        const node = rawNode as SVGGraphicsElement;
                        if (i === 1 || i === 3) {
                            i += 1;
                            continue; // spacing node
                        }
                        const bbox = node.getBBox();
                        const mat = node.transform.baseVal[0];
                        if (mat) {
                            bbox.x += mat.matrix.e;
                        }
                        // move box, and add padding between inference steps
                        if (left == null) {
                            // first box
                            left = bbox.x + bbox.width;
                        } else {
                            // this box has some elements left of it
                            // -> move to free space after other elements
                            mat.matrix.e -= bbox.x - left - padding;
                            left = bbox.x + mat.matrix.e + bbox.width;
                        }
                        if (i == 2) {
                            let parentNode = node.parentNode! as SVGGraphicsElement;
                            while (parentNode.getAttribute("semantics") !== "bspr_inferenceRule:down") {
                                parentNode = parentNode.parentNode! as SVGGraphicsElement;
                            }
                            parentNode = parentNode.children[2] as SVGGraphicsElement;
                            const rule = node.querySelector<SVGGraphicsElement>(inferenceRuleSelector);
                            if (rule) {
                                // this selector should be checked again when updating MathJax
                                const term = rule.children[1].children[0].children[0].children[1].children[0] as SVGGraphicsElement;
                                let w = -parentNode.getTransformToElement(term).e;
                                w += term.getBBox().width;
                                w += padding;
                                parentNode.setAttribute("x2", w.toString());
                            }
                        }
                        i += 1;
                    }
                }
            }
            const conclusion0 = root.querySelector<SVGElement>(inferenceRuleSelector)!.children[1].children[0].children[0].children[1] as SVGGraphicsElement;
            const conclusionWidth = conclusion0.getBBox().width;
            const svgWidth = svg.viewBox.baseVal.width;
            const offset = (svg.children[1] as SVGGraphicsElement).getTransformToElement(conclusion0);
            (svg.children[1] as SVGGraphicsElement).transform.baseVal[0].matrix.e += offset.e + svgWidth / 2 - conclusionWidth / 2;
            console.timeEnd('stepCalculation');

            const thisShadowRoot = this.shadowRoot;
            const hoverTextElID = "typicalc-hover-explainer";
            let defElBackground: SVGRectElement | null;

            if (nodeIterator.length >= 3) {
                // should not be used on empty SVGs
                window.svgPanZoomFun(svg, {
                    fit: false,
                    controlIconsEnabled: true,
                    customEventsHandler: {
                        // Halt all touch events
                        haltEventListeners: ['touchstart', 'touchend', 'touchmove', 'touchleave', 'touchcancel'],

                        // Init custom events handler
                        init: function(options) {
                            const instance = options.instance;
                            // Init Hammer
                            // @ts-ignore
                            this.hammer = Hammer(options.svgElement);

                            // @ts-ignore
                            this.hammer.get('pinch').set({enable: true});

                            // Handle double tap
                            // @ts-ignore
                            this.hammer.on('doubletap', () => {
                                options.instance.zoomIn()
                            });

                            let pannedX = 0;
                            let pannedY = 0;
                            // Handle pan
                            // @ts-ignore
                            this.hammer.on('panstart panmove', ev => {
                                // On pan start reset panned variables
                                if (ev.type === 'panstart') {
                                    pannedX = 0
                                    pannedY = 0
                                }

                                // Pan only the difference
                                instance.panBy({x: ev.deltaX - pannedX, y: ev.deltaY - pannedY})
                                pannedX = ev.deltaX
                                pannedY = ev.deltaY
                                // also move the tooltip
                                let explainer = thisShadowRoot.getElementById(hoverTextElID);
                                if (explainer) {
                                    const ctm1 = svg.getBoundingClientRect();
                                    const ctm2 = defElBackground!.getBoundingClientRect();
                                    explainer.style.left = (ctm2.left - ctm1.left) + "px";
                                    explainer.style.top = (ctm2.bottom - ctm1.top) + "px";
                                    // TODO(performance): this should be more efficient, but somehow flickers
                                    /*
                                    const dx = (ctm2.left - ctm1.left) - explainer.offsetLeft;
                                    const dy = (ctm2.bottom - ctm1.top) - explainer.offsetTop;
                                    explainer.style.transform = "translate(" + dx + "px," + dy + "px)";
                                     */
                                }
                            });

                            let initialScale = 1;
                            // Handle pinch
                            // @ts-ignore
                            this.hammer.on('pinchstart pinchmove', function(ev){
                                // On pinch start remember initial zoom
                                if (ev.type === 'pinchstart') {
                                    initialScale = instance.getZoom()
                                    instance.zoomAtPoint(initialScale * ev.scale, {x: ev.center.x, y: ev.center.y})
                                }

                                instance.zoomAtPoint(initialScale * ev.scale, {x: ev.center.x, y: ev.center.y})
                            });

                            // Prevent moving the page on some devices when panning over SVG
                            options.svgElement.addEventListener('touchmove', function(e: TouchEvent){ e.preventDefault(); });
                        }

                        // Destroy custom events handler
                        , destroy: function(){
                            // @ts-ignore
                            this.hammer.destroy()
                        }
                    }
                });
                // add tooltips to buttons
                const zoomIn = document.createElementNS("http://www.w3.org/2000/svg", "title");
                zoomIn.append(document.createTextNode("zoom in"));
                svg.getElementById("svg-pan-zoom-zoom-in")!.appendChild(zoomIn);
                const zoomOut = document.createElementNS("http://www.w3.org/2000/svg", "title");
                zoomOut.append(document.createTextNode("zoom out"));
                svg.getElementById("svg-pan-zoom-zoom-out")!.appendChild(zoomOut);

                // move control to upper left corner
                let matrix = svg.getElementById("svg-pan-zoom-controls")!.transform.baseVal[0].matrix;
                matrix.e = 0;
                matrix.f = 0;
            }
            this.steps = steps;
            this.showStep(0);

            const viewport = svg.querySelector("#step0")!.parentNode as SVGGraphicsElement;
            const handleMouseEvent = (e: MouseEvent, mouseIn: boolean) => {
                let typeTarget = e.target! as SVGGraphicsElement;
                let counter = 0;
                while (!typeTarget.classList.contains("typicalc-type")
                && !typeTarget.classList.contains("typicalc-label")) {
                    typeTarget = typeTarget.parentNode! as SVGGraphicsElement;
                    counter++;
                    if (counter > 3) {
                        return;
                    }
                }
                let isType = typeTarget.classList.contains("typicalc-type");
                let isLabel = typeTarget.classList.contains("typicalc-label");
                if (mouseIn) {
                    if (isType) {
                        const typeClass = typeTarget.classList[1];
                        const css = "." + typeClass + " { color: red; }";
                        hoverStyles!.innerHTML = css;
                        hoverStylesUnification!.innerHTML = css;
                    } else if (isLabel) {
                        let stepTarget = typeTarget;
                        while (stepTarget.getAttribute("typicalc") !== "step" && counter < 15) {
                            stepTarget = stepTarget.parentNode! as SVGGraphicsElement;
                            counter++;
                        }
                        let stepIndex = stepTarget.getAttribute("typicalc") === "step" ? Number(stepTarget.id.substring(4)) : - 1;

                        const defId = typeTarget.classList[1].replace("-label-", "-definition-");
                        const defEl = this.shadowRoot!.getElementById(defId)! as unknown as SVGGraphicsElement;
                        const transform = viewport.getTransformToElement(typeTarget);
                        const offsetX = -3000;
                        const offsetY = 5500;
                        defEl.style.display = "block";
                        const svgRect = defEl.getBBox();
                        defEl.transform.baseVal[0].matrix.e = -transform.e - svgRect.width + offsetX + 1000;
                        defEl.transform.baseVal[0].matrix.f = -transform.f - 5500 + offsetY;
                        defElBackground = this.shadowRoot!.getElementById(defId + "-background") as SVGRectElement | null;
                        if (!defElBackground) {
                            defElBackground = document.createElementNS("http://www.w3.org/2000/svg", "rect");
                            defElBackground.id = defId + "-background";
                            defElBackground.setAttribute("x", "0");
                            defElBackground.setAttribute("y", "0");
                            defElBackground.setAttribute("width", String(svgRect.width + 2000));
                            defEl.parentElement!.insertBefore(defElBackground, defEl);
                        }
                        defElBackground.setAttribute("x", String(-transform.e - svgRect.width + offsetX));
                        const defElBackgroundY = -transform.f - 7000 + offsetY;
                        defElBackground.setAttribute("y", String(defElBackgroundY));
                        const defElBackgroundHeight = svgRect.height + 1000;
                        defElBackground.setAttribute("height", String(defElBackgroundHeight));
                        defElBackground.setAttribute("fill", "yellow");
                        // calculate screen coordinates
                        const ctm1 = svg.getBoundingClientRect();
                        const ctm2 = defElBackground.getBoundingClientRect();
                        const p = document.createElement("p");
                        p.id = hoverTextElID;
                        p.style.zIndex = String(99);
                        p.style.position = "absolute";
                        p.style.left = (ctm2.left - ctm1.left) + "px";
                        p.style.top = (ctm2.bottom - ctm1.top) + "px";
                        p.style.backgroundColor = "white";
                        p.style.padding = "5px";
                        p.innerText = data[stepIndex];
                        // @ts-ignore
                        window.MathJax.typesetPromise([p])
                            .then(() => {
                                const svgRect2 = defElBackground!.getBBox();

                                const svgP = p.getElementsByTagName("svg")[0];
                                const relevantElement = svgP.childNodes[1]! as SVGGraphicsElement;
                                const relevantElementBox = relevantElement.getBBox();
                                const relevantDefs = svgP.childNodes[0]!;
                                const ourDefs = svg.getElementsByTagName("defs")[0];
                                while (relevantDefs.childNodes.length > 0) {
                                    ourDefs.appendChild(relevantDefs.childNodes[0]);
                                }
                                const insertionTarget = svg.getElementsByClassName("svg-pan-zoom_viewport")[0];
                                // remove previous tooltip, if possible
                                let explainers = svg.getElementsByClassName(hoverTextElID);
                                for (const explainer of explainers) {
                                    explainer.parentNode!.removeChild(explainer);
                                }
                                const g = insertionTarget.ownerDocument.createElementNS("http://www.w3.org/2000/svg", "g");
                                g.setAttribute("transform", "matrix(1 0 0 -1 0 0)");
                                g.transform.baseVal[0].matrix.f -= svgRect2.height;
                                g.classList.add(hoverTextElID);
                                g.appendChild(relevantElement);
                                defEl.appendChild(g);
                                // increase size of tooltip background
                                const padY = 1000;
                                defElBackground!.setAttribute("y", String(defElBackgroundY - relevantElementBox.height - padY));
                                defElBackground!.setAttribute("height",
                                    String(defElBackgroundHeight + relevantElementBox.height + padY));
                                thisShadowRoot.removeChild(p);
                            });
                        this.shadowRoot!.appendChild(p);

                        if (typeTarget.classList.length >= 3) {
                            const stepId = typeTarget.classList[2];
                            hoverStylesUnification!.innerHTML = "." + stepId + " { color: red; }";
                        }
                    }
                } else {
                    if (isType) {
                        hoverStyles!.innerHTML = "";
                        hoverStylesUnification!.innerHTML = "";
                    } else if (isLabel) {
                        // remove previous tooltip, if possible
                        let explainers = svg.getElementsByClassName(hoverTextElID);
                        // do not use a for..of loop, it won't work
                        while (explainers.length > 0) {
                            const exp = explainers[0];
                            exp.parentNode!.removeChild(exp);
                        }
                        const defId = typeTarget.classList[1].replace("-label-", "-definition-");
                        this.shadowRoot!.getElementById(defId)!.style.display = "none";
                        let defElBackground = this.shadowRoot!.getElementById(defId + "-background");
                        defElBackground!.setAttribute("y", String(-10000));
                        defElBackground!.setAttribute("fill", "transparent");
                        if (typeTarget.classList.length >= 3) {
                            hoverStylesUnification!.innerHTML = "";
                        }
                    }
                }
            };
            // listen for hover events on types
            // the class "typicalc-type" is set in LatexCreatorType and in LatexCreatorTerm
            svg.querySelector("#step0")!.addEventListener("mouseover", e => {
                handleMouseEvent(e as MouseEvent, true);
            });
            svg.querySelector("#step0")!.addEventListener("mouseout", e => {
                handleMouseEvent(e as MouseEvent, false);
            });
            // @ts-ignore
            this.shadowRoot.host.handleHoverEvent = (e: MouseEvent, mouseIn: boolean) => {
                handleMouseEvent(e, mouseIn);
            }

            this.$server!.setStepCount(steps.length);
        }
    }
}

customElements.define('tc-proof-tree', MathjaxProofTree);
