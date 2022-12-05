const fs = require("fs");
const path = require("path");

const input = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\r\n\r\n");

const crates = input[0]
    .split("\r\n")
    .slice(0, -1)
    .map(line => line.split("    ").map(c => c.split(" ")).flatMap(c => c).map(c => c.replace("[", "").replace("]", "")));

groups = crates.reduce((prev, cur) => {
    const items = cur.map((c, index) => ({c, index}));
    items.filter(item => !!item.c).forEach(item => (prev[item.index + 1] = prev[item.index + 1] || []).unshift(item.c));
    return prev;
}, {});

const procedure = 
    input[1]
    .split("\r\n")
    .map(line => line.split(" "))
    .map(parts => ( {i: parts[1], from: parts[3], to: parts[5]}));

for (let i = 0; i< procedure.length;i++) {
    step = procedure[i];
    from = groups[step.from];
    to = groups[step.to];
    for (let j = 0; j < step.i; j++) {
        to.push(from.pop());
    }
}

const letters = Object.keys(groups).map(key => groups[key].slice(-1)).join("");
console.log(letters);