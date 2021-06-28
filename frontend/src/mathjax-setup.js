// polyfill deprecated SVG function (not available in Google Chrome)
// source: https://www.jointjs.com/blog/announcement-gettransformtoelement-polyfill
SVGElement.prototype.getTransformToElement = SVGElement.prototype.getTransformToElement || function(elem) {
    return elem.getScreenCTM().inverse().multiply(this.getScreenCTM());
};
window.MathJax = {
    loader: {
        load: ['output/svg', '[tex]/ams', '[tex]/bussproofs', '[tex]/color', '[tex]/textmacros', '[tex]/unicode']
    },
    tex: {
        packages: {'[+]': ['ams', 'bussproofs', 'color', 'textmacros', 'unicode']},
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
                const mjxContainer = root.querySelector<HTMLElement>("mjx-container");
                if (mjxContainer !== null) {
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
                // set the size of the rendered SVG to the size of the container element
                // enlarge hover area of certain elements
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
                        }";
                    style.innerHTML += "svg { width: 100%; }";
                    style.id = "style-fixes";
                    root.querySelector("mjx-head").appendChild(style);
                }
                if (callback) {
                    callback(html);
                }
                const handleMouseEvent = (e, mouseIn) => {
                    let typeTarget = e.target;
                    let counter = 0;
                    while (!typeTarget.classList.contains("typicalc-type")
                        && typeTarget.getAttribute("semantics") !== "bspr_prooflabel:left") {
                        typeTarget = typeTarget.parentElement;
                        counter++;
                        if (counter > 4) {
                            return;
                        }
                    }
                    let isType = typeTarget.classList.contains("typicalc-type");
                    let isLabel = typeTarget.getAttribute("semantics") === "bspr_prooflabel:left";
                    if (mouseIn) {
                        if (isType) {
                            const typeClass = typeTarget.classList[1];
                            hoverStyles.innerHTML = "." + typeClass + " { color: red; }";
                        } else if (isLabel) {
                            typeTarget.style.color = "green";
                        }
                    } else {
                        if (isType) {
                            hoverStyles.innerHTML = "";
                        } else if (isLabel) {
                            typeTarget.style.color = null;
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
