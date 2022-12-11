const fs = require("fs");
const path = require("path");

const monkeys = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\r\n\r\n")
    .map(m => m.split("\r\n"))
    .map(m => ({
        id: parseInt(m[0].match(/\d/g)),
        startingItems: m[1].match(/\d{2}/g).map(i => parseInt(i)),
        operation: m[2].match(/new = old (\*|\+) .*/)[0].replace('new', 'current'),
        test: {
            divisibleBy: parseInt(m[3].match(/\d{1,2}/g)),
            ifTrue: parseInt(m[4].match(/\d/g)),
            ifFalse: parseInt(m[5].match(/\d/g))
        },
        inspections: 0
    }));

const biggestDivisor = monkeys.map(m => m.test.divisibleBy).reduce((a, b) => a * b);
const numberOfRounds = 10000;

for (let round = 1; round <= numberOfRounds; round++) {
    for (const monkey of monkeys) {
        for (const old of monkey.startingItems) {

            monkey.inspections++;
            let current = 0;
            eval(monkey.operation);

            current %= biggestDivisor;

            let targetId;
            if (current % monkey.test.divisibleBy === 0) {  
                targetId = monkey.test.ifTrue;
            } else {
                targetId = monkey.test.ifFalse;
            }

            monkeys.filter(m => m.id === targetId)[0].startingItems.push(current);
        }   
        monkey.startingItems = [];
    }
}

const inspections = monkeys.map(m => m.inspections);
inspections.sort((a, b) => a - b);
const monkeyBusiness = inspections.slice(-2).reduce((a, b) => a * b);
console.log(monkeyBusiness);