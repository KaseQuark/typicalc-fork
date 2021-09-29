// to ease hovering above step labels and types, we increase the usable target area
// in the case of Firefox, we add an invisible stroke to the SVG element
// in the case of Chrome, we set the pointer-events area to the rectangular bounding box of the element
export const hoverAreaAroundElements: string = `
.typicalc-type, g[semantics='bspr_prooflabel:left'] {
	/* cross-browser-compatibility: Chrome does not support the stroke trick, but instead bounding-box (which is not supported by Firefox..) */
	stroke: transparent; stroke-width: 600px; pointer-events: all; pointer-events: bounding-box;
}`;