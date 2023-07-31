let x = 10;
let y = 5;
let a = "apple";
let b = "banana";

console.log(x == y); // false
console.log(x == 10); // true
console.log(x === y); // false (same type but different value)
console.log(x === "10"); // false (different data types)
console.log(x === 10); // true (same data types and value)
console.log(a == b); // false
console.log(a == "apple"); // true
console.log(a === b); // false (same type but different value)
console.log(a === "apple"); // true (same data types and value)
console.log(a === 10); // false (different data types and value)
