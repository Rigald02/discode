const db = mysql.createConnection({
   host: "localhost",
   user: "root",
   password: "root"
 });

  db.connect(function(err) {
    if (err) throw err;
    console.log("Connect to database");
  });
  const dataUser = new DataUser("users", "username", {
    dialect: "mysql",
    host: "localhost",
  })
  const dataMessage = new dataMessage("content", {
    dialect: "mysql",
    host: "localhost",
    })

try {
   dataUser.authenticate();
   dataMessage.authenticate();
   dataUser.query("SELECT username FROM users WHERE friends.friend_user_id = 1;").then(([results, metadata]);
   dataMessage.query("SELECT content FROM messages WHERE user_id = users.id OR friends.friend_user_id = users.id;").
   then(([results, metadata]);
 } catch (error) {
   console.error('Impossible to connect', error);
 }

//here we convert the data taken from the dabatase in a json to read it with the searchbar
  const friend_and_messages = [];
  fetch(dataUser, dataMessage)
    .then(blob => blob.json())
    .then(data => friend_and_messages.push(...data));

  function findMatches(wordToMatch, friend_and_messages) {
    return friend_and_messages.filter(place => {
      // here we need to figure out if the name or content matches what was searched
      const regex = new RegExp(wordToMatch, 'gi');
      return place.username.match(regex) || place.content.match(regex)
    });
  }

  function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  function displayMatches() {
    const matchArray = findMatches(this.value, cities);
    const html = matchArray.map(place => {
      const regex = new RegExp(this.value, 'gi');
      const Name = place.username.replace(regex, `<span class="hl">${this.value}</span>`);
      const Content = place.content.replace(regex, `<span class="hl">${this.value}</span>`);
      return `
        <li>
          <span class="name">${Name}</span>
          <span class="content">, ${Content}</span>
        </li>
      `;
    }).join('');
    suggestions.innerHTML = html;
  }

  const searchInput = document.querySelector('.search');
  const suggestions = document.querySelector('.suggestions');

  searchInput.addEventListener('change', displayMatches);
  searchInput.addEventListener('keyup', displayMatches);