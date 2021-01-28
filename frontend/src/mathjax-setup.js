window.MathJax = {
    loader: {load: ['[tex]/bussproofs', '[tex]/html', '[tex]/action']},
    tex: {
        packages: {'[+]': ['bussproofs', 'html', 'action']},
        inlineMath: [['$', '$'], ['\\(', '\\)']]
    },
    startup: {
        ready: () => {
            MathJax.typesetShadow = function (root, callback) {
                setTimeout(() => {
                let tex = root.getElementById("tc-content").innerText;
                root.getElementById("tc-content").innerText = "";
                MathJax.tex2svgPromise(tex, options).then(function (node) {
                    console.log(node);
                    //
                    //	The promise returns the typeset node, which we add to the output
                    //	Then update the document to include the adjusted CSS for the
                    //		content of the new equation.
                    //
                    root.appendChild(node);
                    MathJax.startup.document.clear();
                    MathJax.startup.document.updateDocument();
                    // TODO: findSteps(node);
                }).catch(function (err) {
                    //
                    //	If there was an error, put the message into the output instead
                    //
                    root.appendChild(document.createElement('pre')).appendChild(document.createTextNode(err.message));
                });}, 1); // TODO: remove the delay?
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
