let socket = null;
let myUsername = "";
let currentTarget = "all";

//Conversaciones separadas
let conversations = {
  all: [],
};

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("send-btn").addEventListener("click", sendMessage);

  document.getElementById("msg-input").addEventListener("keypress", (e) => {
    if (e.key === "Enter") sendMessage();
  });

  // botón conectar
  document
    .querySelector("#login-screen button")
    .addEventListener("click", connect);
});

// CONEXIÓN
function connect() {
  myUsername = document.getElementById("username-input").value.trim();
  if (!myUsername) {
    alert("Escribe un nombre");
    return;
  }

  socket = new WebSocket(
    "wss://uneffaced-nonregimented-karlie.ngrok-free.dev/chat",
  );

  socket.onopen = () => {
    console.log("Conectado al servidor");

    // CONNECT
    socket.send(
      JSON.stringify({
        type: "CONNECT",
        from: myUsername,
      }),
    );

    // Solicitar usuarios
    socket.send(
      JSON.stringify({
        type: "REQUEST_USERS",
      }),
    );

    document.getElementById("login-screen").classList.add("hidden");
    document.getElementById("chat-screen").classList.remove("hidden");
  };

  socket.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data);
      console.log("Recibido:", data);

      switch (data.type) {
        case "USER_LIST":
          updateUserList(data.users || []);
          break;

        case "BROADCAST":
          displayMessage(data, false);
          break;

        case "PRIVATE":
          displayMessage(data, true);
          break;

        case "SYSTEM":
          console.log("Sistema:", data.content);
          break;

        case "ERROR":
          alert("Error del servidor: " + data.content);
          break;
      }
    } catch (e) {
      console.error("Error parseando mensaje:", e);
    }
  };

  socket.onerror = (error) => {
    console.error("Error WebSocket:", error);
  };

  socket.onclose = () => {
    alert("Conexión perdida");
    location.reload();
  };
}

// ENVIAR MENSAJE
function sendMessage() {
  const input = document.getElementById("msg-input");
  const text = input.value.trim();

  if (!text) return;

  if (!socket || socket.readyState !== WebSocket.OPEN) {
    alert("Conexión no disponible");
    return;
  }

  let payload = {
    from: myUsername,
    content: text,
    type: currentTarget === "all" ? "BROADCAST" : "PRIVATE",
  };

  if (currentTarget !== "all") {
    payload.to = currentTarget;
  }

  socket.send(JSON.stringify(payload));

  // Guardar localmente
  displayMessage({ ...payload, isMe: true }, currentTarget !== "all");

  input.value = "";
}

function displayMessage(data, isPrivate) {
  const chatKey = isPrivate ? (data.isMe ? data.to : data.from) : "all";

  if (!conversations[chatKey]) {
    conversations[chatKey] = [];
  }

  conversations[chatKey].push({
    ...data,
    isPrivate,
  });

  if (currentTarget !== chatKey) return;

  renderMessages();
}

// RENDER DE MENSAJES
function renderMessages() {
  const container = document.getElementById("messages-container");
  container.innerHTML = "";

  const msgs = conversations[currentTarget] || [];

  msgs.forEach((data) => {
    const msgDiv = document.createElement("div");

    msgDiv.className = `msg ${data.isMe ? "sent" : "received"} ${data.isPrivate ? "private" : ""}`;

    const sender = data.isMe ? "Tú" : data.from;
    const etiqueta = data.isPrivate ? " (Privado)" : "";

    msgDiv.innerHTML = `<small>${sender}${etiqueta}:</small><br>${data.content}`;

    container.appendChild(msgDiv);
  });

  container.scrollTop = container.scrollHeight;
}

// LISTA DE USUARIOS
function updateUserList(users) {
  const list = document.getElementById("user-list");
  list.innerHTML = "";

  // Global
  const divAll = document.createElement("div");
  divAll.className = `user-item ${currentTarget === "all" ? "active" : ""}`;
  divAll.innerText = "CHAT GLOBAL";
  divAll.onclick = () => setTarget("all");
  list.appendChild(divAll);

  users.forEach((user) => {
    if (user === myUsername) return;

    const div = document.createElement("div");
    div.className = `user-item ${currentTarget === user ? "active" : ""}`;
    div.innerText = user;
    div.onclick = () => setTarget(user);

    list.appendChild(div);
  });
}

// CAMBIAR CHAT
function setTarget(user) {
  currentTarget = user;

  document.getElementById("current-target-label").innerHTML =
    `Mensaje para: <strong>${user === "all" ? "Todos" : user}</strong>`;

  document.querySelectorAll(".user-item").forEach((el) => {
    el.classList.toggle(
      "active",
      el.innerText === user ||
        (user === "all" && el.innerText.includes("GLOBAL")),
    );
  });

  renderMessages();
}
