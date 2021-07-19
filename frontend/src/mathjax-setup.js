// polyfill deprecated SVG function (not available in Google Chrome)
// source: https://www.jointjs.com/blog/announcement-gettransformtoelement-polyfill
SVGElement.prototype.getTransformToElement = SVGElement.prototype.getTransformToElement || function(elem) {
    return elem.getScreenCTM().inverse().multiply(this.getScreenCTM());
};
window.MathJax = {
    loader: {
        load: ['output/svg', '[tex]/ams', '[tex]/bussproofs', '[tex]/color', '[tex]/html', '[tex]/textmacros', '[tex]/unicode']
    },
    tex: {
        packages: {'[+]': ['ams', 'bussproofs', 'color', 'html', 'textmacros', 'unicode']},
        inlineMath: [['$', '$'], ['\\(', '\\)']]
    },
    svg: {
        displayAlign: "left"
    },
    startup: {
        ready: () => {
            const mathjax = MathJax._.mathjax.mathjax;
            const HTMLAdaptor = MathJax._.adaptors.HTMLAdaptor.HTMLAdaptor;
            const HTMLHandler = MathJax._.handlers.html.HTMLHandler.HTMLHandler;
            const AbstractHandler = MathJax._.core.Handler.AbstractHandler.prototype;
            const startup = MathJax.startup;

            //
            //  Extend HTMLAdaptor to handle shadowDOM as the document
            //
            class ShadowAdaptor extends HTMLAdaptor {
                create(kind, ns) {
                    const document = (this.document.createElement ? this.document : this.window.document);
                    return (ns ?
                        document.createElementNS(ns, kind) :
                        document.createElement(kind));
                }

                text(text) {
                    const document = (this.document.createTextNode ? this.document : this.window.document);
                    return document.createTextNode(text);
                }

                head(doc) {
                    return doc.head || (doc.getElementById("mjx-document") || {}).firstChild || doc;
                }

                body(doc) {
                    return doc.body || (doc.getElementById("mjx-document") || {}).lastChild || doc;
                }

                root(doc) {
                    return doc.documentElement || doc.getElementById("mjx-document") || doc;
                }
            }

            //
            //  Extend HTMLHandler to handle shadowDOM as document
            //
            class ShadowHandler extends HTMLHandler {
                create(document, options) {
                    const adaptor = this.adaptor;
                    if (typeof (document) === 'string') {
                        document = adaptor.parse(document, 'text/html');
                    } else if ((document instanceof adaptor.window.HTMLElement ||
                        document instanceof adaptor.window.DocumentFragment) &&
                        !(document instanceof window.ShadowRoot)) {
                        let child = document;
                        document = adaptor.parse('', 'text/html');
                        adaptor.append(adaptor.body(document), child);
                    }
                    //
                    //  We can't use super.create() here, since that doesn't
                    //    handle shadowDOM correctly, so call HTMLHandler's parent class
                    //    directly instead.
                    //
                    return AbstractHandler.create.call(this, document, options);
                }
            }

            //
            //  Register the new handler and adaptor
            //
            startup.registerConstructor('HTMLHandler', ShadowHandler);
            startup.registerConstructor('browserAdaptor', () => new ShadowAdaptor(window));

            //
            //  A service function that creates a new MathDocument from the
            //  shadow root with the configured input and output jax, and then
            //  renders the document.  The MathDocument is returned in case
            //  you need to rerender the shadowRoot later.
            //
            MathJax.typesetShadow = function (root, callback) {
                if (root.getElementById("tc-content") === null) {
                    return;
                }
                const unificationEl = root.host.parentElement.parentElement.querySelector("tc-unification");
                const mjxContainer = root.querySelector<HTMLElement>("mjx-container");
                if (mjxContainer) {
                    mjxContainer.innerHTML = "";
                }
                const InputJax = startup.getInputJax();
                const OutputJax = startup.getOutputJax();
                const html = mathjax.document(root, {InputJax, OutputJax});
                html.render();
                const hostTag = root.host.tagName.toLowerCase();
                if (hostTag !== "tc-proof-tree") {
                    if (callback) {
                        callback(html);
                    }
                    return html;
                }
                // setup style container for styles applied on hover
                let hoverStyles = root.querySelector("#typicalc-hover-styles");
                if (!hoverStyles) {
                    hoverStyles = document.createElement('style');
                    hoverStyles.id = "typicalc-hover-styles";
                    root.querySelector("mjx-head").appendChild(hoverStyles);
                }
                let hoverStylesUnification = unificationEl.shadowRoot.querySelector("#typicalc-hover-styles");
                if (!hoverStylesUnification) {
                    hoverStylesUnification = document.createElement('style');
                    hoverStylesUnification.id = "typicalc-hover-styles";
                    unificationEl.shadowRoot.querySelector("mjx-head").appendChild(hoverStylesUnification);
                }
                // set the size of the rendered SVG to the size of the container element
                // enlarge hover area of certain elements
                //console.log(unificationEl);
                if (!root.querySelector("#style-fixes")) {
                    const style = document.createElement('style');
                    style.innerHTML = "\
                        mjx-doc, mjx-body, mjx-container, #tc-content, svg {\
                            height: 100%;\
                        }\
                        mjx-container {\
                            margin: 0 !important;\
                        }\
                        .typicalc-type, g[semantics='bspr_prooflabel:left'] {\
                            stroke: transparent; stroke-width: 600px; pointer-events: all;\
                        }\
                        #typicalc-definition-abs, #typicalc-definition-abs-let, #typicalc-definition-app,\
                        #typicalc-definition-const, #typicalc-definition-var, #typicalc-definition-var-let, #typicalc-definition-let {\
                            display: none;\
                            border: 2px solid red;\
                        }";
                    style.innerHTML += "svg { width: 100%; }";
                    style.id = "style-fixes";
                    root.querySelector("mjx-head").appendChild(style);
                }
                if (callback) {
                    callback(html);
                }
                const viewport = root.querySelector("#step0").parentElement;
                const handleMouseEvent = (e, mouseIn) => {
                    let typeTarget = e.target;
                    let counter = 0;
                    while (!typeTarget.classList.contains("typicalc-type")
                        && !typeTarget.classList.contains("typicalc-label")) {
                        typeTarget = typeTarget.parentElement;
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
                            hoverStyles.innerHTML = css;
                            hoverStylesUnification.innerHTML = css;
                        } else if (isLabel) {
                            const defId = typeTarget.classList[1].replace("-label-", "-definition-");
                            const defEl = root.getElementById(defId);
                            const transform = viewport.getTransformToElement(typeTarget);
                            const offsetX = -3000;
                            const offsetY = 5500;
                            defEl.style.display = "block";
                            const svgRect = defEl.getBBox();
                            defEl.transform.baseVal[0].matrix.e = -transform.e - svgRect.width + offsetX + 1000;
                            defEl.transform.baseVal[0].matrix.f = -transform.f - 5500 + offsetY;
                            let defElBackground = root.getElementById(defId + "-background");
                            if (!defElBackground) {
                                defElBackground = document.createElementNS("http://www.w3.org/2000/svg", "rect");
                                defElBackground.id = defId + "-background";
                                defElBackground.setAttribute("x", 0);
                                defElBackground.setAttribute("y", 0);
                                defElBackground.setAttribute("width", svgRect.width + 2000);
                                defElBackground.setAttribute("height", svgRect.height + 1000);
                                defEl.parentElement.insertBefore(defElBackground, defEl);
                            }
                            defElBackground.setAttribute("x", -transform.e - svgRect.width + offsetX);
                            defElBackground.setAttribute("y", -transform.f - 7000 + offsetY);
                            defElBackground.setAttribute("fill", "yellow");

                            if (typeTarget.classList.length >= 3) {
                                const stepId = typeTarget.classList[2];
                                hoverStylesUnification.innerHTML = "." + stepId + " { color: red; }";
                            }
                        }
                    } else {
                        if (isType) {
                            hoverStyles.innerHTML = "";
                            hoverStylesUnification.innerHTML = "";
                        } else if (isLabel) {
                            const defId = typeTarget.classList[1].replace("-label-", "-definition-");
                            root.getElementById(defId).style.display = "none";
                            let defElBackground = root.getElementById(defId + "-background");
                            defElBackground.setAttribute("y", 10000);
                            defElBackground.setAttribute("fill", "transparent");
                            if (typeTarget.classList.length >= 3) {
                                hoverStylesUnification.innerHTML = "";
                            }
                        }
                    }
                };
                // listen for hover events on types
                // the class "typicalc-type" is set in LatexCreatorType and in LatexCreatorTerm
                root.querySelector("#step0").addEventListener("mouseover", e => {
                    handleMouseEvent(e, true);
                });
                root.querySelector("#step0").addEventListener("mouseout", e => {
                    handleMouseEvent(e, false);
                });
                return html;
            }

            //
            //  Now do the usual startup now that the extensions are in place
            //
            let event = new Event('mathjax-initialized', {
                bubbles: true,
                composed: true,
                detail: "composed"
            })
            MathJax.startup.defaultReady();
            MathJax.startup.promise.then(() => {
                console.log("MathJax initialized");
                MathJax.isInitialized = true;
                document.dispatchEvent(event);
            })
        }
    }
};
// disable MathJax context menu
window.addEventListener("contextmenu", function (event) {
    event.stopPropagation();
}, true);
