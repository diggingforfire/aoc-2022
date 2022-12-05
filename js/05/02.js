const fs = require("fs");
const path = require("path");

const input = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\r\n\r\n");


const crates = input[0]
    .split("\r\n")
    .slice(0, -1)
    .map(line => line.split("    ").map(c => c.split(" ")).flatMap(c => c));

groups = {};

for (let i = 0; i < crates.length; i++) {
    stack = crates[i];
    for (let j = 0; j < stack.length; j++) {
        if (!groups.hasOwnProperty(j+1)) {
            groups[j+1] = [];
        }
        if (stack[j] != '') {
            groups[j+1].unshift(stack[j])
        }
    }
}
 
const procedure = 
    input[1]
    .split("\r\n")
    .map(line => line.split(" "))
    .map(parts => ( {i: parts[1], from: parts[3], to: parts[5]}));

for (let i = 0; i< procedure.length;i++) {
    step = procedure[i];
    from = groups[step.from];
    to = groups[step.to];
    tmp = [];
    for (let j = 0; j < step.i; j++) {
        tmp.push(from.pop());
    }
    for (let j = 0; j < step.i; j++) {
        to.push(tmp.pop());
    }
}

letters = "";
for (prop in groups) {
    letters += groups[prop].pop().replace("[", "").replace("]", "");
}

console.log(letters);
