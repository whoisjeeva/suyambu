import { stringify } from "../util"


function isPrime(num) {
    for (let i = 2; i < num; i++) {
        if (num % i === 0) {
            return false
        }
    }
    return num > 1
}

export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value1 = await this.executeStatement(stringify(pointer.value1))
        let prop = stringify(pointer.property)

        switch(prop) {
            case "EVEN":
                return value1 % 2 === 0
            case "ODD":
                return value1 % 2 !== 0
            case "PRIME":
                return isPrime(value1)
            case "WHOLE":
                return value1 % 1 === 0
            case "POSITIVE":
                return value1 > 0
            case "NEGATIVE":
                return value1 < 0
            case "DIVISIBLE_BY":
                let value2 = await this.executeStatement(stringify(pointer.value2))
                return value1 % value2 === 0
        }
    } catch (e) {
        onError(e)
    }

    return false
}
