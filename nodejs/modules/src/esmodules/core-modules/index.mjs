import { readFile } from "fs"; // import specific function from module

readFile("/some/path/filename.ext", (err, data) => {
  if (err) throw err;
  console.log(data);
});
