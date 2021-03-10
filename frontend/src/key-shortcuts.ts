document.onkeydown = handleKey;

function handleKey(e: KeyboardEvent) {
	if ((e.target! as HTMLElement).tagName.toLowerCase() === "vaadin-text-field") {
		return;
	}
	let element = null;
	if (e.code === "ArrowLeft") {
		// left arrow
		if (!e.ctrlKey) {
			element = document.getElementById("previous-step");
		} else {
			element = document.getElementById("first-step");
		}
	} else if (e.code === "ArrowRight") {
		// right arrow
		if (!e.ctrlKey) {
			element = document.getElementById("next-step");
		} else {
			element = document.getElementById("last-step");
		}
	} else if (e.key === "/") {
		document.getElementById("inputField")!.focus();
		e.preventDefault();
	}
	if (element !== null) {
		element.click();
		e.preventDefault();
	}
}
