const messageform = document.querySelector(".chatbox form");
const messageList = document.querySelector("#messagelist");
const userList = document.querySelector("ul#users");
const chatboxinput = document.querySelector(".chatbox input");
const socket = io("http://localhost:5000");

let users = [];
let messages = [];
let isUser = "";

socket.on("message", message => {
  messages.push(message);
  updateMessages();
});

socket.on("private", data => {
  isUser = data.name;
});

socket.on("users", function (_users) {
  users = _users;
  updateUsers();
});

messageform.addEventListener("submit", messageSubmitHandler);

function updateUsers() {
  userList.textContent = "";
  for (let i = 0; i < users.length; i++) {
    var node = document.createElement("LI");
    var textnode = document.createTextNode(users[i]);
    node.appendChild(textnode);
    userList.appendChild(node);
  }
}

function updateMessages() {
  messageList.textContent = "";
  for (let i = 0; i < messages.length; i++) {
    const show = isUser === messages[i].user ? true : false;
    messageList.innerHTML += `<li class=${show ? "private" : ""}>
                     <p>${messages[i].user}</p>
                     <p>${messages[i].message}</p>
                       </li>`;
  }
}

function messageSubmitHandler(e) {
  e.preventDefault();
  let message = chatboxinput.value;
  socket.emit("message", message);
  chatboxinput.value = "";
}

function userAddHandler(user) {
  userName = user || `User${Math.floor(Math.random() * 1000000)}`;
  socket.emit("adduser", userName);
}

userAddHandler();
