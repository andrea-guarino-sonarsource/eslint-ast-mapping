const espree = require("espree");
const fs = require("fs");
const { promisify } = require("util");

const readFile = promisify(fs.readFile);
const readDir = promisify(fs.readdir);
const writeFile = promisify(fs.writeFile);

readDir(`${__dirname}/snippets`).then(async files => {
  for (let fileName of files) {
    let content = await readFile(`${__dirname}/snippets/${fileName}`);
    writeFile(
      `${__dirname}/../resources/${fileName}.json`,
      JSON.stringify(
        espree.parse(content, {
          tokens: true,
          loc: true,
          range: true
        })
      )
    );
  }
});
