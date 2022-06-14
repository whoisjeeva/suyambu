import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let name = stringify(pointer.name)
        let args = pointer.args

        this.currentFunctionName = name
        let f = this.findFunction(name)
        let toRet = null

        if (f) {
            let keys = Object.keys(args)
            for (let key of keys) {
                let keySlug = key
                let c = keySlug[0]
                if (!((c >= 'a' && c <= 'z') || (c >= "A" && c <= "Z")) && c !== '_') {
                    keySlug = keySlug.substring(1)
                    keySlug = '_' + keySlug
                }
                keySlug = keySlug.replace(/[^A-Za-z0-9]/g, "_")
                this.VARIABLE_STACK[keySlug] = await this.executeStatement(stringify(args[key]))
            }

            await this.execute(f.functionBlock)

            if (f.ret != null) {
                toRet = await this.executeStatement(stringify(f.ret))
            }
        }

        if (this.functionReturnValue !== null) {
            toRet = functionReturnValue
        }

        this.currentFunctionName = null
        this.isFunctionReturn = null

        return toRet
    } catch (e) {
        onError(e)
    }

    return null
}
