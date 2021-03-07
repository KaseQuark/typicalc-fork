window.autoSelect = (className) => {
	let el = document.getElementsByClassName(className);
	Array.from(el).forEach(field => {
		field.addEventListener('focus', event => {
			let e = event.target.shadowRoot.querySelector('input');
			if (!e) {
				e = event.target.shadowRoot.querySelector('textArea');
			}
			e.setSelectionRange(0, e.value.length);
		});
	});
}