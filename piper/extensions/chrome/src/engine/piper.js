import go_to_website from "./statements/go_to_website"
import download_from_url from "./statements/download_from_url"
import set_user_agent from "./statements/set_user_agent"
import execute_javascript from "./statements/execute_javascript"
import new_tab from "./statements/new_tab"
import switch_tab from "./statements/switch_tab"
import close_tab from "./statements/close_tab"

import html_elements from "./statements/html_elements"
import html_elements_by_selector from "./statements/html_elements_by_selector"
import wait_for_elements from "./statements/wait_for_elements"
import attribute_of from "./statements/attribute_of"
import text_of from "./statements/text_of"
import tag_name_of from "./statements/tag_name_of"
import is_html_element_exist from "./statements/is_html_element_exist"
import page_source from "./statements/page_source"

import click from "./statements/click"
import event_type from "./statements/event_type"
import press_key from "./statements/press_key"
import wait_for from "./statements/wait_for"
import scroll from "./statements/scroll"

import controls_if from "./statements/controls_if"
import logic_compare from "./statements/logic_compare"
import logic_operation from "./statements/logic_operation"
import logic_negate from "./statements/logic_negate"
import logic_boolean from "./statements/logic_boolean"
import logic_ternary from "./statements/logic_ternary"

import controls_repeat_ext from "./statements/controls_repeat_ext"
import controls_whileUntil from "./statements/controls_whileUntil"
import controls_for from "./statements/controls_for"
import controls_forEach from "./statements/controls_forEach"
import controls_flow_statements from "./statements/controls_flow_statements"

import math_number from "./statements/math_number"
import math_arithmetic from "./statements/math_arithmetic"
import math_single from "./statements/math_single"
import math_trig from "./statements/math_trig"
import math_constant from "./statements/math_constant"
import math_number_property from "./statements/math_number_property"
import convert_text_to_number from "./statements/convert_text_to_number"
import math_round from "./statements/math_round"
import math_on_list from "./statements/math_on_list"
import math_modulo from "./statements/math_modulo"
import math_constrain from "./statements/math_constrain"
import math_random_int from "./statements/math_random_int"

import text from "./statements/text"
import text_join from "./statements/text_join"
import text_append from "./statements/text_append"
import text_length from "./statements/text_length"
import text_isEmpty from "./statements/text_isEmpty"
import convert_to_text from "./statements/convert_to_text"
import text_indexOf from "./statements/text_indexOf"
import text_charAt from "./statements/text_charAt"
import text_getSubstring from "./statements/text_getSubstring"
import text_changeCase from "./statements/text_changeCase"
import text_trim from "./statements/text_trim"
import text_print from "./statements/text_print"
import prompt from "./statements/prompt"

import lists_create_with from "./statements/lists_create_with"
import lists_chooser from "./statements/lists_chooser"
import lists_repeat from "./statements/lists_repeat"
import lists_length from "./statements/lists_length"
import lists_isEmpty from "./statements/lists_isEmpty"
import lists_indexOf from "./statements/lists_indexOf"
import lists_getIndex from "./statements/lists_getIndex"
import lists_setIndex from "./statements/lists_setIndex"
import lists_getSublist from "./statements/lists_getSublist"
import lists_split from "./statements/lists_split"
import lists_regex from "./statements/lists_regex"
import lists_sort from "./statements/lists_sort"

import variables_set from "./statements/variables_set"
import variables_get from "./statements/variables_get"
import math_change from "./statements/math_change"

import procedures_defnoreturn from "./statements/procedures_defnoreturn"
import procedures_defreturn from "./statements/procedures_defreturn"


import { sleep } from "./util"


class Piper {
    constructor(browser) {
        this.browser = browser
        this.UA = null

        this.loopFlow = null
        this.currentFunctionName = null
        this.isFunctionReturn = null
        this.functionReturnValue = null

        this.VARIABLE_STACK = {}
        this.FUNCTION_STACK = []
        this.isTerminated = false
        this.canShowErrorAlert = true

        this.browser.onClose(windowId => {
            this.isTerminated = true
        })
    }

    async showError(statement, error) {
        this.isTerminated = true
    }

    async onScriptComplete() {
        console.log("Completed")
    }

    async init(code) {
        await sleep(500)
        this.browser.loadUrl("file:///C:/Piper/index.html")
        await sleep(500)
        this.browser.loadUrl("file:///C:/Piper/index.html")
        let isSuccess = this.initFunctions(code)
        if (isSuccess) {
            await this.execute(code)
        }
        await this.onScriptComplete()
    }

    async initFunctions(code) {
        let statements = null
        try {
            statements = JSON.parse(code)
        } catch (e) {
            this.showError({ cmd: "function_init" }, e)
            return false
        }

        for (let statement of statements) {
            if (statement.cmd === "procedures_defnoreturn") {
                await procedures_defnoreturn.call(this, statement, e => { this.showError(statement, e) })
            } else if (statement.cmd === "procedures_defreturn") {
                await procedures_defreturn.call(this, statement, e => { this.showError(statement, e) })
            }
        }
        return true
    }

    async execute(sourceCode) {
        let statements = JSON.parse(sourceCode)
        for (let statement of statements) {
            if (this.isTerminated) break
            if (this.loopFlow === "BREAK") {
                break
            } else if (this.loopFlow === "CONTINUE") {
                continue
            }

            if (this.isFunctionReturn !== null) break

            await this.executeStatement(statement)
        }
    }

    async executeStatement(statement) {
        await this.browser.loadingWait()
        if (typeof statement === "string") {
            try {
                statement = JSON.parse(statement)
            } catch (e) {
                return statement
            }
        }

        if (statement.cmd === undefined) {
            return statement
        }

        console.log(statement)

        switch (statement.cmd) {

            // Browser
            case "go_to_website":
                return await go_to_website.call(this, statement, e => { this.showError(statement, e) })
            case "download_from_url":
                return await download_from_url.call(this, statement, e => { this.showError(statement, e) })
            case "set_user_agent":
                return await set_user_agent.call(this, statement, e => { this.showError(statement, e) })
            case "execute_javascript":
                return await execute_javascript.call(this, statement, e => { this.showError(statement, e) })
            case "new_tab":
                return await new_tab.call(this, statement, e => { this.showError(statement, e) })
            case "switch_tab":
                return await switch_tab.call(this, statement, e => { this.showError(statement, e) })
            case "close_tab":
                return await close_tab.call(this, statement, e => { this.showError(statement, e) })


            // HTML
            case "html_elements":
                return await html_elements.call(this, statement, e => { this.showError(statement, e) })
            case "html_elements_by_selector":
                return await html_elements_by_selector.call(this, statement, e => { this.showError(statement, e) })
            case "wait_for_elements":
                return await wait_for_elements.call(this, statement, e => { this.showError(statement, e) })
            case "attribute_of":
                return await attribute_of.call(this, statement, e => { this.showError(statement, e) })
            case "text_of":
                return await text_of.call(this, statement, e => { this.showError(statement, e) })
            case "tag_name_of":
                return await tag_name_of.call(this, statement, e => { this.showError(statement, e) })
            case "is_html_element_exist":
                return await is_html_element_exist.call(this, statement, e => { this.showError(statement, e) })
            case "page_source":
                return await page_source.call(this, statement, e => { this.showError(statement, e) })


            // Events
            case "click":
                return await click.call(this, statement, e => { this.showError(statement, e) })
            case "type":
                return await event_type.call(this, statement, e => { this.showError(statement, e) })
            case "press_key":
                return await press_key.call(this, statement, e => { this.showError(statement, e) })
            case "wait_for":
                return await wait_for.call(this, statement, e => { this.showError(statement, e) })
            case "scroll":
                return await scroll.call(this, statement, e => { this.showError(statement, e) })

            
            // Logic
            case "controls_if":
                return await controls_if.call(this, statement, e => { this.showError(statement, e) })
            case "logic_compare":
                return await logic_compare.call(this, statement, e => { this.showError(statement, e) })
            case "logic_operation":
                return await logic_operation.call(this, statement, e => { this.showError(statement, e) })
            case "logic_negate":
                return await logic_negate.call(this, statement, e => { this.showError(statement, e) })
            case "logic_boolean":
                return await logic_boolean.call(this, statement, e => { this.showError(statement, e) })
            case "logic_null":
                return null
            case "logic_ternary":
                return await logic_ternary.call(this, statement, e => { this.showError(statement, e) })

            
            // Loops
            case "controls_repeat_ext":
                return await controls_repeat_ext.call(this, statement, e => { this.showError(statement, e) })
            case "controls_whileUntil":
                return await controls_whileUntil.call(this, statement, e => { this.showError(statement, e) })
            case "controls_for":
                return await controls_for.call(this, statement, e => { this.showError(statement, e) })
            case "controls_forEach":
                return await controls_forEach.call(this, statement, e => { this.showError(statement, e) })
            case "controls_flow_statements":
                return await controls_flow_statements.call(this, statement, e => { this.showError(statement, e) })


            // Math
            case "math_number":
                return await math_number.call(this, statement, e => { this.showError(statement, e) })
            case "math_arithmetic":
                return await math_arithmetic.call(this, statement, e => { this.showError(statement, e) })
            case "math_single":
                return await math_single.call(this, statement, e => { this.showError(statement, e) })
            case "math_trig":
                return await math_trig.call(this, statement, e => { this.showError(statement, e) })
            case "math_constant":
                return await math_constant.call(this, statement, e => { this.showError(statement, e) })
            case "math_number_property":
                return await math_number_property.call(this, statement, e => { this.showError(statement, e) })
            case "convert_text_to_number":
                return await convert_text_to_number.call(this, statement, e => { this.showError(statement, e) })
            case "math_round":
                return await math_round.call(this, statement, e => { this.showError(statement, e) })
            case "math_on_list":
                return await math_on_list.call(this, statement, e => { this.showError(statement, e) })
            case "math_modulo":
                return await math_modulo.call(this, statement, e => { this.showError(statement, e) })
            case "math_constrain":
                return await math_constrain.call(this, statement, e => { this.showError(statement, e) })
            case "math_random_int":
                return await math_random_int.call(this, statement, e => { this.showError(statement, e) })
            case "math_random_float":
                return await math_random_float.call(this, statement, e => { this.showError(statement, e) })


            // Text
            case "text":
                return await text.call(this, statement, e => { this.showError(statement, e) })
            case "text_join":
                return await text_join.call(this, statement, e => { this.showError(statement, e) })
            case "text_append":
                return await text_append.call(this, statement, e => { this.showError(statement, e) })
            case "text_length":
                return await text_length.call(this, statement, e => { this.showError(statement, e) })
            case "text_isEmpty":
                return await text_isEmpty.call(this, statement, e => { this.showError(statement, e) })
            case "convert_to_text":
                return await convert_to_text.call(this, statement, e => { this.showError(statement, e) })
            case "text_indexOf":
                return await text_indexOf.call(this, statement, e => { this.showError(statement, e) })
            case "text_charAt":
                return await text_charAt.call(this, statement, e => { this.showError(statement, e) })
            case "text_getSubstring":
                return await text_getSubstring.call(this, statement, e => { this.showError(statement, e) })
            case "text_changeCase":
                return await text_changeCase.call(this, statement, e => { this.showError(statement, e) })
            case "text_trim":
                return await text_trim.call(this, statement, e => { this.showError(statement, e) })
            case "text_print":
                return await text_print.call(this, statement, e => { this.showError(statement, e) })
            case "prompt":
                return await prompt.call(this, statement, e => { this.showError(statement, e) })

            
            // Lists
            case "lists_create_with":
                return await lists_create_with.call(this, statement, e => { this.showError(statement, e) })
            case "lists_chooser":
                return await lists_chooser.call(this, statement, e => { this.showError(statement, e) })
            case "lists_repeat":
                return await lists_repeat.call(this, statement, e => { this.showError(statement, e) })
            case "lists_length":
                return await lists_length.call(this, statement, e => { this.showError(statement, e) })
            case "lists_isEmpty":
                return await lists_isEmpty.call(this, statement, e => { this.showError(statement, e) })
            case "lists_indexOf":
                return await lists_indexOf.call(this, statement, e => { this.showError(statement, e) })
            case "lists_getIndex":
                return await lists_getIndex.call(this, statement, e => { this.showError(statement, e) })
            case "lists_setIndex":
                return await lists_setIndex.call(this, statement, e => { this.showError(statement, e) })
            case "lists_getSublist":
                return await lists_getSublist.call(this, statement, e => { this.showError(statement, e) })
            case "lists_split":
                return await lists_split.call(this, statement, e => { this.showError(statement, e) })
            case "lists_regex":
                return await lists_regex.call(this, statement, e => { this.showError(statement, e) })
            case "lists_sort":
                return await lists_sort.call(this, statement, e => { this.showError(statement, e) })


            // Variables
            case "variables_set":
                return await variables_set.call(this, statement, e => { this.showError(statement, e) })
            case "variables_get":
                return await variables_get.call(this, statement, e => { this.showError(statement, e) })
            case "math_change":
                return await math_change.call(this, statement, e => { this.showError(statement, e) })


            default:
                return null
        }
    }
}


export { Piper }
