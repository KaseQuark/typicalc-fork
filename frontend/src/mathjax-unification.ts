import {MathjaxAdapter} from "./mathjax-adapter";
import {TemplateResult} from "lit-html";
import {html} from "lit-element";

class MathjaxUnification extends MathjaxAdapter {
    private content: String = "Loading...";
    private latex: String[] = [];

    static get properties() {
        return {
            content: {type: String}
        }
    }

    render(): TemplateResult {
        return html`<mjx-doc id="mjx-document"><mjx-head></mjx-head><mjx-body>
            <div id="tc-content">${this.content}</div>
        </mjx-body></mjx-doc>`;
    }

    protected showStep(n: number): void {
        this.content = this.latex[n];
        this.updateComplete.then(() => this.execTypeset(this.shadowRoot));

    }

    public setTex(tex: String): void {
        console.log(tex);
        this.latex.push(tex);
        console.log(this.latex)
    }
}

customElements.define('tc-unification', MathjaxUnification);