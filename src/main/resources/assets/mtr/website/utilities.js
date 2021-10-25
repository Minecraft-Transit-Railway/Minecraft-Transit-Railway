const convertColor = color => "#" + Number(color).toString(16).padStart(6, "0");
const isCJK = text => text.match(/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uff9f\u4e00-\u9faf\u3400-\u4dbf]/);
const isBetween = (x, a, b) => x >= Math.min(a, b) && x <= Math.max(a, b);
const getColorStyle = style => parseInt(getComputedStyle(document.body).getPropertyValue(style).replace(/#/g, ""), 16);

{
	var scaleUpperLimit = 64;
	var scaleLowerLimit = 1 / 128;
	var dragThreshold = 10;
	var steps = 50;

	var scale = 1;
	var textCache = {};

	var dragging = false;
	var dragCounter = 0;
	var mouseClickX = 0;
	var mouseClickY = 0;

	var step = 0;
	var startX = 0;
	var startY = 0;
	var targetX = undefined;
	var targetY = undefined;

	function convertX(x) {
		return Math.floor((x + window.innerWidth / 2) * scale / LINE_SIZE) * LINE_SIZE;
	}

	function convertY(y) {
		return Math.floor((y + window.innerHeight / 2) * scale / LINE_SIZE) * LINE_SIZE;
	}

	function onCanvasMouseDown(event) {
		dragging = true;
		mouseClickX = event.data.global.x;
		mouseClickY = event.data.global.y;
		targetX = undefined;
		targetY = undefined;
	}

	function onCanvasMouseMove(event, container) {
		if (dragging) {
			const mouseX = event.data.global.x;
			const mouseY = event.data.global.y;

			if (isBetween(mouseX, 0, window.innerWidth) && isBetween(mouseY, 0, window.innerHeight)) {
				const changeX = mouseX - mouseClickX;
				const changeY = mouseY - mouseClickY;
				container.x += changeX;
				container.y += changeY;
				dragCounter += Math.abs(changeX) + Math.abs(changeY);
				mouseClickX = event.data.global.x;
				mouseClickY = event.data.global.y;
			} else {
				onCanvasMouseUp();
			}
		}

		return dragCounter > dragThreshold;
	}

	function onCanvasMouseUp() {
		dragging = false;
		dragCounter = 0;
	}

	function onCanvasScroll(event, container) {
		onZoom(Math.sign(event.deltaY), event.offsetX, event.offsetY, container);
	}

	function onZoom(amount, offsetX, offsetY, container) {
		scale *= amount < 0 ? 2 : 0.5;
		scale = Math.min(scaleUpperLimit, Math.max(scaleLowerLimit, scale));
		container.x -= Math.round((offsetX - container.x) / (amount < 0 ? 1 : -2));
		container.y -= Math.round((offsetY - container.y) / (amount < 0 ? 1 : -2));
	}

	function slideTo(container, x, y) {
		startX = container.x;
		startY = container.y;
		targetX = Math.round(x);
		targetY = Math.round(y);
		step = 0;
	}

	function update(delta, container) {
		if (typeof targetX !== "undefined" && typeof targetY !== "undefined") {
			step += delta;
			const speed = step >= steps / 2 ? 4 - 4 * step / steps : 4 * step / steps;
			container.x += speed * (targetX - startX) / steps;
			container.y += speed * (targetY - startY) / steps;
			if (step >= steps) {
				container.x = targetX;
				container.y = targetY;
				targetX = undefined;
				targetY = undefined;
			}
		}
	}
}

{


	function drawText(textArray, text, x, y) {
		const textSplit = text.split("|");
		let yStart = y;
		for (const index in textSplit) {
			const textPart = textSplit[index];
			const isTextCJK = isCJK(textPart);

			if (typeof textCache[textPart] === "undefined") {
				const richText = new PIXI.Text(textPart, {
					fontFamily: getComputedStyle(document.body).fontFamily,
					fontSize: (isTextCJK ? 3 : 1.5) * LINE_SIZE,
					fill: getColorStyle("--textColor"),
					stroke: getColorStyle("--backgroundColor"),
					strokeThickness: 2,
				});
				richText.anchor.set(0.5, 0);
				textCache[textPart] = richText;
			}

			const richText = textCache[textPart];
			richText.position.set(Math.round(x / 2) * 2, yStart);
			textArray.push(richText);
			yStart += (isTextCJK ? 3 : 1.5) * LINE_SIZE;
		}
	}
}

function drawLine(graphics, x1, y1, vertical1, x2, y2, vertical2) {
	const differenceX = Math.abs(x2 - x1);
	const differenceY = Math.abs(y2 - y1);
	const points = [];
	points.push({x: x1, y: y1});
	if (differenceX > differenceY) {
		if (vertical1 && vertical2) {
			const midpoint = (y1 + y2) / 2;
			points.push({x: x1 + Math.sign(x2 - x1) * Math.abs(midpoint - y1), y: midpoint});
			points.push({x: x2 - Math.sign(x2 - x1) * Math.abs(midpoint - y1), y: midpoint});
		} else if (!vertical1 && !vertical2) {
			points.push({x: x1 + Math.sign(x2 - x1) * (differenceX - differenceY) / 2, y: y1});
			points.push({x: x2 - Math.sign(x2 - x1) * (differenceX - differenceY) / 2, y: y2});
		} else if (vertical1) {
			points.push({x: x1 + Math.sign(x2 - x1) * differenceY, y: y2});
		} else {
			points.push({x: x2 - Math.sign(x2 - x1) * differenceY, y: y1});
		}
	} else {
		if (vertical1 && vertical2) {
			points.push({x: x1, y: y1 + Math.sign(y2 - y1) * (differenceY - differenceX) / 2});
			points.push({x: x2, y: y2 - Math.sign(y2 - y1) * (differenceY - differenceX) / 2});
		} else if (!vertical1 && !vertical2) {
			const midpoint = (x1 + x2) / 2;
			points.push({x: midpoint, y: y1 + Math.sign(y2 - y1) * Math.abs(midpoint - x1)});
			points.push({x: midpoint, y: y2 - Math.sign(y2 - y1) * Math.abs(midpoint - x1)});
		} else if (vertical1) {
			points.push({x: x1, y: y2 - Math.sign(y2 - y1) * differenceX});
		} else {
			points.push({x: x2, y: y1 + Math.sign(y2 - y1) * differenceX});
		}
	}
	points.push({x: x2, y: y2});

	graphics.moveTo(x1, y1);

	const offset1 = LINE_SIZE / 2;
	const offset2 = offset1 / Math.sqrt(2);

	let reverse = false;
	do {
		let i = 0;
		while (i < points.length - 1) {
			const thisPoint = points[reverse ? points.length - 1 - i : i];
			const nextPoint = points[reverse ? points.length - 2 - i : i + 1];
			const thisX = thisPoint["x"];
			const thisY = thisPoint["y"];
			const nextX = nextPoint["x"];
			const nextY = nextPoint["y"];

			const signX = Math.sign(nextY - thisY);
			const signY = Math.sign(thisX - nextX);
			const offsetX = Math.round(signX * (signX !== 0 && signY !== 0 ? offset2 : offset1));
			const offsetY = Math.round(signY * (signX !== 0 && signY !== 0 ? offset2 : offset1));

			graphics.lineTo(thisX + offsetX, thisY + offsetY);
			graphics.lineTo(nextX + offsetX, nextY + offsetY);

			i++;
		}
		reverse = !reverse;
	} while (reverse);
}
