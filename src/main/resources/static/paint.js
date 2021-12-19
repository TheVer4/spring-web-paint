const CSRF = document.getElementById("CSRF").value;

const listGetParamters = () => {
    const queryDict = {};
    location.search.substr(1).split("&").forEach(function(item) {queryDict[item.split("=")[0]] = item.split("=")[1]});
    return queryDict;
};

const width = window.screen.width*0.98;
const height = window.screen.height*0.75;

const BOARD_ID = listGetParamters().id;

const stage = new Konva.Stage({
    container: 'container',
    width: width,
    height: height,
});

const layer = new Konva.Layer();

stage.add(layer);

const generateId = () => {
    return (new Date()).getTime();
};

const getRandomColor = () => {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

const drawLine = ({coords, color, id}) => {
    coords = JSON.parse(coords);
    let line = new Konva.Line({
        points: coords,
        stroke: color,
        strokeWidth: 15,
        lineCap: 'round',
        lineJoin: 'round',
        id: id,
        name: 'line',
    });
    layer.add(line);
    line.draw();
};

const drawText = ({coords, text, color, id}) => {
    coords = JSON.parse(coords);
    let kText = new Konva.Text({
        y: coords.pop(),
        x: coords.pop(),
        text: text,
        fontFamily: 'Calibri',
        fontSize: 18,
        fill: color,
        padding: 5,
        id: id,
        name: 'text',
    });

    layer.add(kText);
};

const contents = [];

const tr = new Konva.Transformer({
    resizeEnabled: false,
    rotateEnabled: false,
});
layer.add(tr);

const container = stage.container();
container.tabIndex = 1;
container.focus();

const listeners = [];

const removeDrawing = (drawing) => {
    drawing.remove();
    layer.batchDraw();
};

stage.on('mousedown.select', (e) => {
    const drawing = e.target;
    const DELETE_KEY = 46;
    if (!(drawing.hasName('line') || drawing.hasName('text'))) {
        tr.nodes([]);
        for(let listener of listeners) {
            container.removeEventListener('keydown', listener);
        }
    }
    else {
        tr.nodes([drawing]);
        layer.batchDraw();
        let listener = (keyEvent) => {
            if (keyEvent.keyCode === DELETE_KEY && (drawing.hasName('line') || drawing.hasName('text'))) {
                console.log('PRESSED DELETE');
                console.log(drawing);
                requestDeleteDrawing(drawing.attrs.id)
                tr.nodes([]);
            }
        };
        for(let listener of listeners) {
            container.removeEventListener('keydown', listener);
        }
        listeners.push(listener)
        container.addEventListener('keydown', listener);
    }
});

const click_add_line = () => {
    document.getElementById("addLine").disabled = true;
    stage.on('mousedown.addline', (evtStr, callback) => {
        let mousePos = stage.getPointerPosition();
        let newClicks = [mousePos.x, mousePos.y];
        console.log("found click: ", newClicks);
        contents.push(...newClicks);
        if(contents.length === 4) {
            console.log("all clicks found: ", contents);
            let lineId = 'line' + generateId();
            console.log(lineId);
            let data = {coords: contents.slice(0, 4), color: getRandomColor(), drawing_type: "LINE"};
            requestCreateDrawing(data);
            contents.splice(0, contents.length);
            stage.off('mousedown.addline', callback);
            document.getElementById("addLine").disabled = false;
        }
    });
    console.log("begin line drawing");
}

const click_add_text = () => {
    stage.on('mousedown.addtext', (evtStr, callback) => {
        let mousePos = stage.getPointerPosition();
        let newClicks = [mousePos.x, mousePos.y];
        console.log("found click: ", newClicks);
        contents.push(...newClicks);
        console.log("all clicks found: ", contents);
        let textId = 'text' + generateId();
        console.log(textId);
        const textValue = prompt("Введите текст:");
        let data = {coords: contents.slice(0, 4), text: textValue, color: getRandomColor(), drawing_type: "TEXT"};
        requestCreateDrawing(data);
        contents.splice(0, contents.length);
        stage.off('mousedown.addtext', callback);
    });
}

const clear_canvas = () => {
    layer.removeChildren();
    layer.add(tr);
}

const click_clear_canvas = () => {
    requestDeleteAllDrawings();
    clear_canvas();
    requestDrawingsList();
};

const requestDrawingsList = async () => {
    const response = await fetch(`/drawing/${BOARD_ID}`, {
        method: "GET",
        headers: { "Accept": "application/json" }
    });
    if (response.ok === true) {

        let list = await response.json();

        clear_canvas();

        for (let drawing of list) {
            switch (drawing.drawing_type) {
                case "LINE":
                    drawLine(drawing);
                    break;
                case "TEXT":
                    drawText(drawing);
                    break;
                default:
                    console.error(`Unknown drawing type: ${drawing.type}`);
                    console.log(drawing);
            }
        }

    }
};

const requestCreateDrawing = async (data) => {
    let form = new FormData();
    form.append("_csrf", CSRF);
    form.append("json", JSON.stringify(data));
    const response = await fetch(`/drawing/${BOARD_ID}`, {
        method: "POST",
        headers: { "Accept": "application/json" },
        body: form,
    });
    if(response.ok) {}
};

const requestDeleteDrawing = async (drawing_id) => {
    let form = new FormData();
    form.append("_csrf", CSRF);
    const response = await fetch(`/drawing/${BOARD_ID}/${drawing_id}`, {
        method: "DELETE",
        headers: { "Accept": "application/json" },
        body: form,
    });
    if(response.ok) {}
};

const requestDeleteAllDrawings = async () => {
    let form = new FormData();
    form.append("_csrf", CSRF);
    const response = await fetch(`/drawing/${BOARD_ID}`, {
        method: "DELETE",
        headers: { "Accept": "application/json" },
        body: form,
    });
    if(response.ok) {}
};

document.getElementById("addLine").addEventListener("click", click_add_line);

document.getElementById("addText").addEventListener("click", click_add_text);

document.getElementById("clearCanvas").addEventListener("click", click_clear_canvas);

setInterval(requestDrawingsList, 1000);