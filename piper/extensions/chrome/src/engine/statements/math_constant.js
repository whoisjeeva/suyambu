import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let constant = stringify(pointer.const)

        switch(constant) {
            case "PI":
                return Math.PI
            case "E":
                return Math.E
            case "GOLDEN_RATIO":
                return (1 + Math.sqrt(5)) / 2
            case "SQRT2":
                return Math.SQRT2
            case "SQRT1_2":
                return Math.SQRT1_2
            case "INFINITY":
                return Infinity
        }
    } catch (e) {
        onError(e)
    }

    return null
}
