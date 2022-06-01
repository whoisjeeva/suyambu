Blockly.Blocks['go_to_website'] = {
    init: function() {
        this.appendValueInput("URL")
            .setCheck("String")
            .appendField("go to website")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#E82634")
        this.setTooltip("Navigate to a website")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['go_to_website'] = function(block) {
    var url = Blockly.JavaScript.valueToCode(block, 'URL', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "go_to_website", pointer: url }
    return JSON.stringify(code) + ","
}

Blockly.Blocks['download_from_url'] = {
    init: function() {
        this.appendValueInput("URL")
            .setCheck("String")
            .appendField("download from url")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#E82634")
        this.setTooltip("Trigger download of a file from a URL")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['download_from_url'] = function(block) {
    var url = Blockly.JavaScript.valueToCode(block, 'URL', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "download_from_url", pointer: url }
    return JSON.stringify(code) + ","
}

Blockly.Blocks['set_user_agent'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("set user-agent to")
            .appendField(new Blockly.FieldDropdown([["desktop","DESKTOP"], ["mobile","MOBILE"]]), "OP")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#E82634")
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['set_user_agent'] = function(block) {
    var op = block.getFieldValue('OP')
    var code = { cmd: "set_user_agent", pointer: op }
    return JSON.stringify(code) + ","
}

Blockly.Blocks['custom_user_agent'] = {
    init: function() {
        this.appendValueInput("AGENT")
            .setCheck("String")
            .appendField("set user-agent to")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#E82634")
        this.setTooltip("")
        this.setHelpUrl("")
    }
}


Blockly.JavaScript['custom_user_agent'] = function(block) {
    var agent = Blockly.JavaScript.valueToCode(block, 'AGENT', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "custom_user_agent", pointer: agent }
    return JSON.stringify(code) + ","
}

Blockly.Blocks['execute_javascript'] = {
    init: function() {
        this.appendValueInput("SCRIPT")
            .setCheck("String")
            .appendField("execute javascript")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#E82634")
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['execute_javascript'] = function(block) {
    var script = Blockly.JavaScript.valueToCode(block, 'SCRIPT', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "execute_javascript", pointer: script }
    return JSON.stringify(code) + ","
}

Blockly.Blocks['new_tab'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("open a new tab")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#E82634")
        this.setTooltip("Open a new tab in the browser")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript["new_tab"] = function(block) {
    var code = { cmd: "new_tab" }
    return JSON.stringify(code) + ","
}

Blockly.Blocks['switch_tab'] = {
    init: function() {
        this.appendValueInput("INDEX")
            .setCheck("Number")
            .appendField("switch to tab")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#E82634")
        this.setTooltip("Switch to a tab in the browser")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript["switch_tab"] = function(block) {
    var index = Blockly.JavaScript.valueToCode(block, 'INDEX', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "switch_tab", pointer: index }
    return JSON.stringify(code) + ","
}


Blockly.Blocks['close_tab'] = {
    init: function() {
        this.appendValueInput("INDEX")
            .setCheck("Number")
            .appendField("close tab")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#E82634")
        this.setTooltip("Close a tab in the browser")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript["close_tab"] = function(block) {
    var index = Blockly.JavaScript.valueToCode(block, 'INDEX', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "close_tab", pointer: index }
    return JSON.stringify(code) + ","
}


/* HTML */

Blockly.Blocks['html_elements'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("html elements")
        this.setOutput(true, "Array")
        this.setColour(20)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['html_elements'] = function(block) {
    var code = { cmd: "html_elements", pointer: JSON.parse(Gumify.stack[block.id]) }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['html_elements_by_selector'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("html elements by selector")
            .appendField(new Blockly.FieldTextInput("a[href]"), "SELECTOR")
        this.setOutput(true, "Array")
        this.setColour(20)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['html_elements_by_selector'] = function(block) {
    let selector = block.getFieldValue('SELECTOR')
    let code = { cmd: "html_elements_by_selector", pointer: selector }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['attribute_of'] = {
    init: function() {
        this.appendValueInput("ELEMENTS")
            .setCheck("Array")
            .appendField("attribute")
            .appendField(new Blockly.FieldTextInput("href"), "ATTR_NAME")
            .appendField("of")
            .appendField(new Blockly.FieldDropdown([["all","ALL"], ["first","FIRST"], ["last","LAST"]]), "OP")
        this.setOutput(true, ["String", "Array"])
        this.setColour(20)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['attribute_of'] = function(block) {
    var attr_name = block.getFieldValue('ATTR_NAME');
    var op = block.getFieldValue('OP');
    var elements = Blockly.JavaScript.valueToCode(block, 'ELEMENTS', Blockly.JavaScript.ORDER_NONE)

    let code = { cmd: "attribute_of", pointer: { elements: elements, attr: attr_name, op: op } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['text_of'] = {
    init: function() {
        this.appendValueInput("ELEMENTS")
            .setCheck("Array")
            .appendField("text of")
            .appendField(new Blockly.FieldDropdown([["all","ALL"], ["first","FIRST"], ["last","LAST"]]), "OP")
        this.setOutput(true, ["String", "Array"])
        this.setColour(20)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['text_of'] = function(block) {
    let elements = Blockly.JavaScript.valueToCode(block, 'ELEMENTS', Blockly.JavaScript.ORDER_NONE)
    let op = block.getFieldValue('OP')
    let code = { cmd: "text_of", pointer: {elements: elements, op: op}}
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['tag_name_of'] = {
    init: function() {
        this.appendValueInput("ELEMENTS")
            .setCheck("Array")
            .appendField("tag name of")
            .appendField(new Blockly.FieldDropdown([["all","ALL"], ["first","FIRST"], ["last","LAST"]]), "OP")
        this.setOutput(true, ["String", "Array"])
        this.setColour(20)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['tag_name_of'] = function(block) {
    let elements = Blockly.JavaScript.valueToCode(block, 'ELEMENTS', Blockly.JavaScript.ORDER_NONE)
    let op = block.getFieldValue('OP')
    let code = { cmd: "tag_name_of", pointer: { elements: elements, op: op } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['wait_for_elements'] = {
    init: function() {
        this.appendValueInput("ELEMENTS")
            .setCheck("Array")
            .appendField("wait for")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour(20)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['wait_for_elements'] = function(block) {
    var elements = Blockly.JavaScript.valueToCode(block, 'ELEMENTS', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "wait_for_elements", pointer: elements }
    return JSON.stringify(code) + ","
}

Blockly.Blocks['is_html_element_exist'] = {
    init: function() {
        this.appendValueInput("ELEMENTS")
            .setCheck("Array")
            .appendField("is")
        this.appendDummyInput()
            .appendField("exist?")
        this.setInputsInline(true)
        this.setOutput(true, "Boolean")
        this.setColour(20)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['is_html_element_exist'] = function(block) {
    var elements = Blockly.JavaScript.valueToCode(block, 'ELEMENTS', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "is_html_element_exist", pointer: elements }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['page_source'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("page source as text")
        this.setOutput(true, "String")
        this.setColour(20)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['page_source'] = function(block) {
    var code = { cmd: "page_source" }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}



/* Events */

Blockly.Blocks['click'] = {
    init: function() {
        this.appendValueInput("ELEMENTS")
            .setCheck("Array")
            .appendField(new Blockly.FieldDropdown([["wait","WAIT"], ["don't wait","DONT_WAIT"]]), "OP")
            .appendField("and click on")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour(80)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['click'] = function(block) {
    var op = block.getFieldValue('OP')
    var elements = Blockly.JavaScript.valueToCode(block, 'ELEMENTS', Blockly.JavaScript.ORDER_NONE) || []
    var code = { cmd: "click", pointer: { elements: elements, op: op } }
    return JSON.stringify(code) + ","
}

Blockly.Blocks['type'] = {
    init: function() {
        this.appendValueInput("VALUE")
            .setCheck(null)
            .appendField("type")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour(80)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['type'] = function(block) {
    var value = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "type", pointer: value }
    return JSON.stringify(code) + ","
}


Blockly.Blocks['press_key'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("press key")
            .appendField(new Blockly.FieldDropdown([["enter","ENTER"], ["shift","SHIFT"], ["ctrl","CTRL"], ["caps lock","CAPS_LOCK"], ["tab","TAB"], ["esc","ESC"]]), "KEY");
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour(80)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['press_key'] = function(block) {
    var key = block.getFieldValue('KEY')
    var code = { cmd: "press_key", pointer: key }
    return JSON.stringify(code) + ","
}


Blockly.Blocks['wait_for'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("wait for")
            .appendField(new Blockly.FieldTextInput("1"), "SECONDS")
            .appendField("seconds")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour(80)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['wait_for'] = function(block) {
    var number_sec = block.getFieldValue('SECONDS')
    var code = { cmd: "wait_for", pointer: number_sec }
    return JSON.stringify(code) + ","
}


Blockly.Blocks['scroll'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("scroll")
            .appendField(new Blockly.FieldDropdown([["up","UP"], ["down","DOWN"]]), "OP")
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour(80)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['scroll'] = function(block) {
    var op = block.getFieldValue('OP')
    var code = { cmd: "scroll", pointer: op }
    return JSON.stringify(code) + ","
}



/* Logic */ 

Blockly.JavaScript["controls_if"] = function(block) {
    let code = []
    for (let input of block.inputList) {
        if (input.name.startsWith("IF")) {
            var ifCond = Blockly.JavaScript.valueToCode(block, input.name, Blockly.JavaScript.ORDER_NONE) || "false"
            code.push({ cond: ifCond })
        } else if (input.name.startsWith("DO")) {
            var ifBlock = Blockly.JavaScript.statementToCode(block, input.name)
            if (ifBlock.endsWith(",")) {
                ifBlock = ifBlock.slice(0, -1)
            }
            code[code.length - 1].do = "[" + ifBlock + "]"
        } else if (input.name === "ELSE") {
            var elseBlock = Blockly.JavaScript.statementToCode(block, input.name)
            if (elseBlock.endsWith(",")) {
                elseBlock = elseBlock.slice(0, -1)
            }
            code.push({ else: "[" + elseBlock + "]" })
        }
    }
    return JSON.stringify({ cmd: "controls_if", pointer: code }) + ","
}


/* TODO: Change the default value from "0" to object */
Blockly.JavaScript["logic_compare"] = function(block) {
    let op = { EQ: "==", NEQ: "!=", LT: "<", LTE: "<=", GT: ">", GTE: ">=" } [block.getFieldValue("OP")]
    let order = Blockly.JavaScript.ORDER_NONE
    let value1 = Blockly.JavaScript.valueToCode(block, "A", order) || "0"
    let value2 = Blockly.JavaScript.valueToCode(block, "B", order) || "0"
    let code = { op: op, value1: value1, value2: value2 }
    return [JSON.stringify({ cmd: "logic_compare", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}


Blockly.JavaScript["logic_operation"] = function(block) {
    let op = { AND: "&&", OR: "||" } [block.getFieldValue("OP")]
    let order = Blockly.JavaScript.ORDER_NONE
    let value1 = Blockly.JavaScript.valueToCode(block, "A", order) || { cmd: "logic_boolean", pointer: false }
    let value2 = Blockly.JavaScript.valueToCode(block, "B", order) || { cmd: "logic_boolean", pointer: false }
    let code = { op: op, value1: value1, value2: value2 }
    return [JSON.stringify({ cmd: "logic_operation", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}


Blockly.JavaScript["logic_negate"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let value = Blockly.JavaScript.valueToCode(block, "BOOL", order) || { cmd: "logic_boolean", pointer: false }
    return [JSON.stringify({ cmd: "logic_negate", pointer: value }), Blockly.JavaScript.ORDER_NONE]
}


Blockly.JavaScript["logic_boolean"] = function(block) {
    let value = block.getFieldValue("BOOL")
    if (value === "TRUE") {
        value = true
    } else {
        value = false
    }
    return [JSON.stringify({ cmd: "logic_boolean", pointer: value }), Blockly.JavaScript.ORDER_NONE]
}


Blockly.JavaScript["logic_null"] = function(block) {
    return [JSON.stringify({ cmd: "logic_null", pointer: null }), Blockly.JavaScript.ORDER_NONE]
}


Blockly.JavaScript["logic_ternary"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let cond = Blockly.JavaScript.valueToCode(block, "IF", order) || { cmd: "logic_boolean", pointer: false }
    let value1 = Blockly.JavaScript.valueToCode(block, "THEN", order) || { cmd: "logic_null", pointer: null }
    let value2 = Blockly.JavaScript.valueToCode(block, "ELSE", order) || { cmd: "logic_null", pointer: null }
    let code = { op: "?", cond: cond, value1: value1, value2: value2 }
    return [JSON.stringify({ cmd: "logic_ternary", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}



/* Loops */ 

Blockly.JavaScript["controls_repeat_ext"] = function(block) {
    let times = Blockly.JavaScript.valueToCode(block, "TIMES", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 0 }
    let loopBlock = Blockly.JavaScript.statementToCode(block, "DO")
    if (loopBlock.endsWith(",")) {
        loopBlock = loopBlock.slice(0, -1)
    }
    let code = { times: times, loopBlock: "[" + loopBlock + "]" }
    return JSON.stringify({ cmd: "controls_repeat_ext", pointer: code }) + ","
}

Blockly.JavaScript["controls_whileUntil"] = function(block) {
    let mode = block.getFieldValue("MODE")
    let order = Blockly.JavaScript.ORDER_NONE
    let value = Blockly.JavaScript.valueToCode(block, "BOOL", order) || { cmd: "logic_boolean", pointer: false }
    let loopBlock = Blockly.JavaScript.statementToCode(block, "DO")
    if (loopBlock.endsWith(",")) {
        loopBlock = loopBlock.slice(0, -1)
    }
    let code = { cond: value, value: value, mode: mode, loopBlock: "[" + loopBlock + "]" }
    return JSON.stringify({ cmd: "controls_whileUntil", pointer: code }) + ","
}

Blockly.JavaScript["controls_for"] = function(block) {
    let variable = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("VAR"), Blockly.VARIABLE_CATEGORY_NAME)
    let order = Blockly.JavaScript.ORDER_NONE
    let from = Blockly.JavaScript.valueToCode(block, "FROM", order) || { cmd: "math_number", pointer: 0 }
    let to = Blockly.JavaScript.valueToCode(block, "TO", order) || { cmd: "math_number", pointer: 0 }
    let step = Blockly.JavaScript.valueToCode(block, "BY", order) || { cmd: "math_number", pointer: 1 }
    let loopBlock = Blockly.JavaScript.statementToCode(block, "DO")
    if (loopBlock.endsWith(",")) {
        loopBlock = loopBlock.slice(0, -1)
    }
    let code = { variable: variable, from: from, to: to, step: step, loopBlock: "[" + loopBlock + "]" }
    return JSON.stringify({ cmd: "controls_for", pointer: code }) + ","
}

Blockly.JavaScript["controls_forEach"] = function(block) {
    let variable = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("VAR"), Blockly.VARIABLE_CATEGORY_NAME)
    let order = Blockly.JavaScript.ORDER_NONE
    let list = Blockly.JavaScript.valueToCode(block, "LIST", order) || { cmd: "list_create_with", pointer: [] }
    let loopBlock = Blockly.JavaScript.statementToCode(block, "DO")
    if (loopBlock.endsWith(",")) {
        loopBlock = loopBlock.slice(0, -1)
    }
    let code = { variable: variable, list: list, loopBlock: "[" + loopBlock + "]" }
    return JSON.stringify({ cmd: "controls_forEach", pointer: code }) + ","
}

Blockly.JavaScript["controls_flow_statements"] = function(block) {
    let flow = block.getFieldValue("FLOW")
    let code = { flow: flow }
    return JSON.stringify({ cmd: "controls_flow_statements", pointer: code }) + ","
}



/* Math */ 

Blockly.JavaScript["math_number"] = function(block) {
    return [JSON.stringify({ cmd: "math_number", pointer: block.getFieldValue("NUM") }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_arithmetic"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let value1 = Blockly.JavaScript.valueToCode(block, "A", order) || { cmd: "math_number", pointer: 0 }
    let value2 = Blockly.JavaScript.valueToCode(block, "B", order) || { cmd: "math_number", pointer: 0 }
    let op = block.getFieldValue("OP")
    let code = { op: op, value1: value1, value2: value2 }
    return [JSON.stringify({ cmd: "math_arithmetic", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_single"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let value = Blockly.JavaScript.valueToCode(block, "NUM", order) || { cmd: "math_number", pointer: 0 }
    let op = block.getFieldValue("OP")
    let code = { op: op, value: value }
    return [JSON.stringify({ cmd: "math_single", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_trig"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let value = Blockly.JavaScript.valueToCode(block, "NUM", order) || { cmd: "math_number", pointer: 0 }
    let op = block.getFieldValue("OP")
    let code = { op: op, value: value }
    return [JSON.stringify({ cmd: "math_trig", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_constant"] = function(block) {
    let code = { const: block.getFieldValue("CONSTANT") }
    return [JSON.stringify({ cmd: "math_constant", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_number_property"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let value1 = Blockly.JavaScript.valueToCode(block, "NUMBER_TO_CHECK", order) || { cmd: "math_number", pointer: 0 }
    let value2 = Blockly.JavaScript.valueToCode(block, "DIVISOR", order) || { cmd: "math_number", pointer: 0 }
    let property = block.getFieldValue("PROPERTY")
    let code = { property: property, value1: value1, value2: value2 }
    return [JSON.stringify({ cmd: "math_number_property", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['convert_text_to_number'] = {
    init: function() {
        this.appendValueInput("TEXT")
            .setCheck("String")
            .appendField("convert")
        this.appendDummyInput()
            .appendField("to number")
        this.setOutput(true, "Number")
        this.setColour(230)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['convert_text_to_number'] = function(block) {
    var text = Blockly.JavaScript.valueToCode(block, 'TEXT', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "convert_text_to_number", pointer: text }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_round"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let op = block.getFieldValue("OP")
    let value = Blockly.JavaScript.valueToCode(block, "NUM", order) || { cmd: "math_number", pointer: 0 }
    let code = { value: value, op: op }
    return [JSON.stringify({ cmd: "math_round", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_on_list"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let list = Blockly.JavaScript.valueToCode(block, "LIST", order) || { cmd: "list_create_with", pointer: [] }
    let op = block.getFieldValue("OP")
    let code = { op: op, list: list }
    return [JSON.stringify({ cmd: "math_on_list", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_modulo"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let value1 = Blockly.JavaScript.valueToCode(block, "DIVIDEND", order) || { cmd: "math_number", pointer: 0 }
    let value2 = Blockly.JavaScript.valueToCode(block, "DIVISOR", order) || { cmd: "math_number", pointer: 0 }
    let code = { value1: value1, value2: value2 }
    return [JSON.stringify({ cmd: "math_modulo", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_constrain"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", order) || { cmd: "math_number", pointer: 0 }
    let min = Blockly.JavaScript.valueToCode(block, "LOW", order) || { cmd: "math_number", pointer: 0 }
    let max = Blockly.JavaScript.valueToCode(block, "HIGH", order) || { cmd: "math_number", pointer: 0 }
    let code = { value: value, min: min, max: max }
    return [JSON.stringify({ cmd: "math_constrain", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_random_int"] = function(block) {
    let order = Blockly.JavaScript.ORDER_NONE
    let min = Blockly.JavaScript.valueToCode(block, "FROM", order) || { cmd: "math_number", pointer: 0 }
    let max = Blockly.JavaScript.valueToCode(block, "TO", order) || { cmd: "math_number", pointer: 0 }
    let code = { min: min, max: max }
    return [JSON.stringify({ cmd: "math_random_int", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["math_random_float"] = function(block) {
    return [JSON.stringify({ cmd: "math_random_float" }), Blockly.JavaScript.ORDER_NONE]
}


/* Text */

Blockly.JavaScript["text"] = function(block) {
    return [JSON.stringify({ cmd: "text", pointer: block.getFieldValue("TEXT") }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_join"] = function(block) {
    let list = []
    for (let i = 0; i < block.inputList.length; i++) {
        let value = Blockly.JavaScript.valueToCode(block, "ADD" + i, Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
        list.push(value)
    }
    return [JSON.stringify({ cmd: "text_join", pointer: list }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_append"] = function(block) {
    let variable = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("VAR"), Blockly.Variables.NAME_TYPE)
    let text = Blockly.JavaScript.valueToCode(block, "TEXT", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let code = { cmd: "text_append", pointer: { variable: variable, text: text } }
    return JSON.stringify(code) + ","
}

Blockly.JavaScript["text_length"] = function(block) {
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let code = { cmd: "text_length", pointer: value }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_isEmpty"] = function(block) {
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let code = { cmd: "text_isEmpty", pointer: value }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['convert_to_text'] = {
    init: function() {
        this.appendValueInput("VALUE")
            .setCheck(null)
            .appendField("convert")
        this.appendDummyInput()
            .appendField("to text")
        this.setOutput(true, "String")
        this.setColour(160)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['convert_to_text'] = function(block) {
    var VALUE = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "convert_to_text", pointer: VALUE }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_indexOf"] = function(block) {
    let indexOf = block.getFieldValue("END")
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let substring = Blockly.JavaScript.valueToCode(block, "FIND", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let code = { cmd: "text_indexOf", pointer: { value: value, substring: substring, indexOf: indexOf } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_charAt"] = function(block) {
    let action = block.getFieldValue("WHERE") || "FROM_START"
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let at = Blockly.JavaScript.valueToCode(block, "AT", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 0 }
    let code = { cmd: "text_charAt", pointer: { value: value, at: at, action: action } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_getSubstring"] = function(block) {
    let action1 = block.getFieldValue("WHERE1") || { cmd: "math_number", pointer: 0 }
    let action2 = block.getFieldValue("WHERE2") || { cmd: "math_number", pointer: 0 }
    let value = Blockly.JavaScript.valueToCode(block, "STRING", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let at1 = Blockly.JavaScript.valueToCode(block, "AT1", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 0 }
    let at2 = Blockly.JavaScript.valueToCode(block, "AT2", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 0 }
    
    let code = { cmd: "text_getSubstring", pointer: { value: value, at1: at1, at2: at2, action1: action1, action2: action2 } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_changeCase"] = function(block) {
    let action = block.getFieldValue("CASE")
    let value = Blockly.JavaScript.valueToCode(block, "TEXT", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let code = { cmd: "text_changeCase", pointer: { value: value, action: action } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_trim"] = function(block) {
    let action = block.getFieldValue("MODE")
    let value = Blockly.JavaScript.valueToCode(block, "TEXT", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let code = { cmd: "text_trim", pointer: { value: value, action: action } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["text_print"] = function(block) {
    let value = Blockly.JavaScript.valueToCode(block, "TEXT", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
    let code = { cmd: "text_print", pointer: value }
    return JSON.stringify(code) + ","
}

// Blockly.JavaScript["text_prompt_ext"] = function(block) {
//     let type = block.getFieldValue("TYPE")
//     let label = Blockly.JavaScript.valueToCode(block, "TEXT", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "" }
//     let code = { cmd: "text_prompt_ext", pointer: { type: type, label: label } }
//     return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
// }

Blockly.Blocks['prompt'] = {
    init: function() {
        this.appendDummyInput()
            .appendField("prompt")
        this.appendValueInput("VALUE")
            .setCheck(null)
            .appendField("with value")
        this.appendValueInput("MESSAGE")
            .setCheck(null)
            .appendField("with message")
        this.setInputsInline(false)
        this.setOutput(true, "String")
        this.setColour(160)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['prompt'] = function(block) {
    var value = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_NONE)
    var message = Blockly.JavaScript.valueToCode(block, 'MESSAGE', Blockly.JavaScript.ORDER_NONE)
    var code = { cmd: "prompt", pointer: { value: value, message: message } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}


/* Lists */ 

Blockly.JavaScript["lists_create_with"] = function(block) {
    let items = []
    for (let i = 0; i < block.itemCount_; i++) {
        let item = Blockly.JavaScript.valueToCode(block, "ADD" + i, Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_null", pointer: null }
        items.push(item)
    }
    return [JSON.stringify({ cmd: "lists_create_with", pointer: items }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['lists_chooser'] = {
    init: function() {
        this.appendValueInput("LIST")
            .setCheck("Array")
            .appendField("prompt options")
        this.setOutput(true, "Number")
        this.setColour(260)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['lists_chooser'] = function(block) {
    var list = Blockly.JavaScript.valueToCode(block, 'LIST', Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    var code = { cmd: "lists_chooser", pointer: list }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["lists_repeat"] = function(block) {
    let value = Blockly.JavaScript.valueToCode(block, "ITEM", Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_null", pointer: null }
    let times = Blockly.JavaScript.valueToCode(block, "NUM", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 0 }
    return [JSON.stringify({ cmd: "lists_repeat", pointer: { value: value, times: times } }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["lists_length"] = function(block) {
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    return [JSON.stringify({ cmd: "lists_length", pointer: value }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["lists_isEmpty"] = function(block) {
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    return [JSON.stringify({ cmd: "lists_isEmpty", pointer: value }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["lists_indexOf"] = function(block) {
    let list = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    let item = Blockly.JavaScript.valueToCode(block, "FIND", Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_null", pointer: null }
    let indexOf = block.getFieldValue("END")
    return [JSON.stringify({ cmd: "lists_indexOf", pointer: { list: list, item: item, indexOf: indexOf } }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["lists_getIndex"] = function(block) {
    let list = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    let mode = block.getFieldValue("MODE")
    let where = block.getFieldValue("WHERE")
    let index = Blockly.JavaScript.valueToCode(block, "AT", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 1 }
    if (mode === "REMOVE") {
        return JSON.stringify({ cmd: "lists_getIndex", pointer: { list: list, index: index, mode: mode, where: where } }) + ","
    }
    return [JSON.stringify({ cmd: "lists_getIndex", pointer: { list: list, index: index, mode: mode, where: where } }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["lists_setIndex"] = function(block) {
    let list = Blockly.JavaScript.valueToCode(block, "LIST", Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    let value = Blockly.JavaScript.valueToCode(block, "TO", Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_null", pointer: null }
    let mode = block.getFieldValue("MODE")
    let where = block.getFieldValue("WHERE")
    let index = Blockly.JavaScript.valueToCode(block, "AT", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 1 }
    return JSON.stringify({ cmd: "lists_setIndex", pointer: { list: list, value: value, index: index, mode: mode, where: where } }) + ","
}

Blockly.JavaScript["lists_getSublist"] = function(block) {
    let list = Blockly.JavaScript.valueToCode(block, "LIST", Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    let where1 = block.getFieldValue("WHERE1")
    let where2 = block.getFieldValue("WHERE2")
    let at1 = Blockly.JavaScript.valueToCode(block, "AT1", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 1 }
    let at2 = Blockly.JavaScript.valueToCode(block, "AT2", Blockly.JavaScript.ORDER_NONE) || { cmd: "math_number", pointer: 1 }
    return [JSON.stringify({ cmd: "lists_getSublist", pointer: { list: list, where1: where1, where2: where2, at1: at1, at2: at2 } }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["lists_split"] = function(block) {
    let delim = Blockly.JavaScript.valueToCode(block, "DELIM", Blockly.JavaScript.ORDER_NONE) || { cmd: "text", pointer: "," }
    let mode = block.getFieldValue("MODE")
    let value = Blockly.JavaScript.valueToCode(block, "INPUT", Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    return [JSON.stringify({ cmd: "lists_split", pointer: { value: value, mode: mode, delim: delim } }), Blockly.JavaScript.ORDER_NONE]
}

Blockly.Blocks['lists_regex'] = {
    init: function() {
        this.appendValueInput("TEXT")
            .setCheck("String")
            .appendField("in")
        this.appendDummyInput()
            .appendField("find all")
            .appendField(new Blockly.FieldTextInput("[a-z]"), "REGEX")
        this.setInputsInline(true)
        this.setOutput(true, "Array")
        this.setColour(260)
        this.setTooltip("")
        this.setHelpUrl("")
    }
}

Blockly.JavaScript['lists_regex'] = function(block) {
    var text = Blockly.JavaScript.valueToCode(block, 'TEXT', Blockly.JavaScript.ORDER_NONE)
    var regex = block.getFieldValue('REGEX')
    var code = { cmd: "lists_regex", pointer: { text: text, regex: regex } }
    return [JSON.stringify(code), Blockly.JavaScript.ORDER_NONE]
}

Blockly.JavaScript["lists_sort"] = function(block) {
    let list = Blockly.JavaScript.valueToCode(block, "LIST", Blockly.JavaScript.ORDER_NONE) || { cmd: "lists_create_with", pointer: [] }
    let direction = block.getFieldValue("DIRECTION")
    let type = block.getFieldValue("TYPE")
    return [JSON.stringify({ cmd: "lists_sort", pointer: { list: list, direction: direction, type: type } }), Blockly.JavaScript.ORDER_NONE]
}


/* Variable */ 

Blockly.JavaScript["variables_set"] = function(block) {
    let variable = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("VAR"), Blockly.VARIABLE_CATEGORY_NAME)
    let order = Blockly.JavaScript.ORDER_NONE
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", order) || { cmd: "logic_null", pointer: null }
    let code = { variable: variable, value: value }
    return JSON.stringify({ cmd: "variables_set", pointer: code }) + ","
}

Blockly.JavaScript["math_change"] = function(block) {
    let variable = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("VAR"), Blockly.VARIABLE_CATEGORY_NAME)
    let order = Blockly.JavaScript.ORDER_NONE
    let value = Blockly.JavaScript.valueToCode(block, "DELTA", order) || { cmd: "math_number", pointer: 0 }
    let code = { variable: variable, value: value }
    return JSON.stringify({ cmd: "math_change", pointer: code }) + ","
}

Blockly.JavaScript["variables_get"] = function(block) {
    let variable = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("VAR"), Blockly.VARIABLE_CATEGORY_NAME)
    return [JSON.stringify({ cmd: "variables_get", pointer: variable }), Blockly.JavaScript.ORDER_NONE]
}


/* Functions */ 

Blockly.JavaScript["procedures_defnoreturn"] = function(block) {
    let name = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("NAME"), Blockly.PROCEDURE_CATEGORY_NAME)
    let functionBlock = Blockly.JavaScript.statementToCode(block, "STACK")
    if (functionBlock.endsWith(",")) {
        functionBlock = functionBlock.slice(0, -1)
    }
    let args = []
    for (let i = 0; i < block.arguments_.length; i++) {
        args[i] = Blockly.JavaScript.variableDB_.getName(block.arguments_[i], Blockly.VARIABLE_CATEGORY_NAME)
    }
    let code = { name: name, args: args, functionBlock: "[" + functionBlock + "]" }
    return JSON.stringify({ cmd: "procedures_defnoreturn", pointer: code }) + ","
}

// console.log(String(Blockly.JavaScript.procedures_callreturn))
Blockly.JavaScript["procedures_callnoreturn"] = function(block) {
    let name = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("NAME"), Blockly.PROCEDURE_CATEGORY_NAME)
    let args = {}
    for (let i = 0; i < block.argumentVarModels_.length; i++) {
        let variable = Blockly.JavaScript.variableDB_.getName(block.argumentVarModels_[i].id_, Blockly.VARIABLE_CATEGORY_NAME)
        args[variable] = Blockly.JavaScript.valueToCode(block, "ARG" + i, Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_null", pointer: null }
    }

    let code = { name: name, args: args }
    return JSON.stringify({ cmd: "procedures_callnoreturn", pointer: code }) + ","
}

Blockly.JavaScript["procedures_ifreturn"] = function(block) {
    let condition = Blockly.JavaScript.valueToCode(block, "CONDITION", Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_boolean", pointer: false }
    let value = Blockly.JavaScript.valueToCode(block, "VALUE", Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_null", pointer: null }

    let code = { condition: condition, value: value }
    return JSON.stringify({ cmd: "procedures_ifreturn", pointer: code }) + ","   
}

Blockly.JavaScript["procedures_defreturn"] = function(block) {
    let name = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("NAME"), Blockly.PROCEDURE_CATEGORY_NAME)
    let ret = Blockly.JavaScript.valueToCode(block, "RETURN", Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_null", pointer: null }
    let functionBlock = Blockly.JavaScript.statementToCode(block, "STACK")
    if (functionBlock.endsWith(",")) {
        functionBlock = functionBlock.slice(0, -1)
    }
    let args = []
    for (let i = 0; i < block.arguments_.length; i++) {
        args[i] = Blockly.JavaScript.variableDB_.getName(block.arguments_[i], Blockly.VARIABLE_CATEGORY_NAME)
    }
    let code = { name: name, args: args, functionBlock: "[" + functionBlock + "]", ret: ret }
    return JSON.stringify({ cmd: "procedures_defreturn", pointer: code }) + ","
}


Blockly.JavaScript["procedures_callreturn"] = function(block) {
    let arg_names = block.arguments_
    let name = Blockly.JavaScript.variableDB_.getName(block.getFieldValue("NAME"), Blockly.PROCEDURE_CATEGORY_NAME)
    let args = {}
    for (let i = 0; i < arg_names.length; i++) {
        args[arg_names[i]] = Blockly.JavaScript.valueToCode(block, "ARG" + i, Blockly.JavaScript.ORDER_NONE) || { cmd: "logic_null", pointer: null }
    }
    
    let code = { name: name, args: args }
    return [JSON.stringify({ cmd: "procedures_callreturn", pointer: code }), Blockly.JavaScript.ORDER_NONE]
}



/* Utils */

Blockly.Blocks['utils_tryCatch'] = {
    init: function() {
        this.appendStatementInput("TRY")
            .setCheck(null)
            .appendField("try")
        this.appendStatementInput("CATCH")
            .setCheck(null)
            .appendField("catch")
        this.setInputsInline(false)
        this.setPreviousStatement(true, null)
        this.setNextStatement(true, null)
        this.setColour("#e67e22")
        this.setTooltip("")
        this.setHelpUrl("")
    }
}


Blockly.JavaScript['utils_tryCatch'] = function(block) {
    var tryBlock = Blockly.JavaScript.statementToCode(block, 'TRY')
    var catchBlock = Blockly.JavaScript.statementToCode(block, 'CATCH')
    if (tryBlock.endsWith(",")) {
        tryBlock = tryBlock.slice(0, -1)
    }
    if (catchBlock.endsWith(",")) {
        catchBlock = catchBlock.slice(0, -1)
    }
    var code = { cmd: "utils_tryCatch", pointer: { try: "[" + tryBlock + "]", catch: "[" + catchBlock + "]" } }
    return JSON.stringify(code) + ","
}
