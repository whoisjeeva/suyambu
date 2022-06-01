import { stringify } from "../util"



function listsGetSortCompare(type, direction) {
    var compareFuncs = {
      'NUMERIC': function(a, b) {
          return Number(a) - Number(b); },
      'TEXT': function(a, b) {
          return a.toString() > b.toString() ? 1 : -1; },
      'IGNORE_CASE': function(a, b) {
          return a.toString().toLowerCase() > b.toString().toLowerCase() ? 1 : -1; },
    };
    var compare = compareFuncs[type];
    return function(a, b) { return compare(a, b) * direction; };
}


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let list = await this.executeStatement(stringify(pointer.list))
        let direction = pointer.direction
        let sortType = stringify(pointer.type)
       
        return list.slice().sort(listsGetSortCompare(sortType, direction))
    } catch (e) {
        onError(e)
    }
}
