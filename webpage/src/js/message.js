const container = document.createElement('div');
container.className = 'message-popup-container';

let trans = 0;

const node = document.querySelector('#app')
node.before(container)

let popupSet = new Set();
let timer;
let timer_end = 0;

function reset() {
    container.style.transition = "0s";
    trans = 0;
    for (let popup of popupSet) {
        popup.remove();
    }
    popupSet.clear()
    container.style.transform = "translateY(-" + trans + "px)";
    container.style.transition = ".5s";
}

function tip(icon, msg, timeout, className) {
    const popup = document.createElement('div');
    popup.innerHTML = `<div style="margin-right: 5px">${icon}</div><div>${msg}</div>`;
    popup.className = "message-popup " + className;
    container.append(popup);
    popupSet.add(popup);
    let tmp_end = Date.now() + timeout + 550;
    if (tmp_end > timer_end) {
        clearTimeout(timer);
        timer = setTimeout(reset, timeout + 550);
        timer_end = tmp_end;
    }
    setTimeout(() => {
        setTimeout(() => {
            trans += (popup.offsetHeight + 10)
            container.style.transform = "translateY(-" + trans + "px)";
            // popup.remove();
        }, 500)
        popup.className = popup.className + " message-popup-fade";
    }, timeout)
}

function confirm(theme, message, finish, buttons) {
    const back = document.createElement('div');
    back.className = "message-confirm-back";
    node.before(back);
    const popup = document.createElement('div');
    popup.className = "message-confirm";
    back.append(popup);

    function emit(state) {
        back.style.opacity = "0";
        back.style.pointerEvents = "none";
        popup.style.transform = "translateY(-50%)";
        setTimeout(() => back.remove(), 400);
        finish(state);
    }

    const title = document.createElement('div');
    title.className = "message-confirm-title";
    title.innerHTML = theme;
    popup.append(title);
    const close = document.createElement('span');
    close.className = "message-confirm-close icon-cancel-circle";
    close.onclick = () => emit(0);

    title.append(close);
    const msg = document.createElement('div');
    msg.className = "message-confirm-msg";
    msg.innerHTML = message;
    popup.append(msg);
    const btnPart = document.createElement('div');
    btnPart.className = "message-confirm-btn-part";
    popup.append(btnPart);

    for (let i in buttons) {
        const button = document.createElement('div');
        button.innerHTML = buttons[i];
        button.className = "message-confirm-btn";
        button.onclick = () => emit(+i + 1);
        btnPart.append(button)
    }
}

export default {
    success(msg, timeout = 2000) {
        tip("<span class='icon-checkmark'></span>", msg, timeout, "message-success");
    },
    error(msg, timeout = 2000) {
        tip("<span class='icon-cancel-circle'></span>", msg, timeout, "message-error");
    },
    tip(msg, timeout = 2000) {
        tip("<span class='icon-info'></span>", msg, timeout, "message-tip");
    },
    confirm
}
