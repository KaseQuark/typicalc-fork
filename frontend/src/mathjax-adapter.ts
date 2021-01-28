import {LitElement, html} from 'lit-element';
import {TemplateResult} from "lit-html";
declare let window: {
    MathJax: {
        typesetShadow: (arg0: ShadowRoot | null, arg1: () => void) => void,
        isInitialized: boolean,
    } | undefined;
    addEventListener: (arg0: string, arg1: () => void) => void;
};

export abstract class MathjaxAdapter extends LitElement {
    protected execTypeset() {
        if (window.MathJax !== undefined) {
            window.MathJax.typesetShadow(this.shadowRoot, () => this.calculateSteps());
        }
    }
            // @ts-ignore


    render(): TemplateResult {
        return html`<mjx-doc id="mjx-document"><mjx-head></mjx-head><mjx-body>
            <div id="tc-content"></div>
        </mjx-body></mjx-doc>`;
    }

    protected abstract showStep(n: number): void;

    protected abstract calculateSteps(): void;

    connectedCallback() {
        super.connectedCallback();
        if (window.MathJax === undefined || !window.MathJax.isInitialized) {
            window.addEventListener('mathjax-initialized', () => this.execTypeset());
        } else {
            this.execTypeset();
        }
    }
}

