var BLOCKLY_TOOLBOX_XML = BLOCKLY_TOOLBOX_XML || Object.create(null);


BLOCKLY_TOOLBOX_XML['standard'] = `
<xml id="toolbox" style="display: none">
    <category name="Browser" colour="#E82634">
        <block type="go_to_website">
            <value name="URL">
                <shadow type="text">
                    <field name="TEXT"></field>
                </shadow>
            </value>
        </block>
        <block type="download_from_url"></block>
        <block type="set_user_agent"></block>
        <block type="custom_user_agent">
            <value name="AGENT">
                <shadow type="text">
                    <field name="TEXT"></field>
                </shadow>
            </value>
        </block>
        <block type="execute_javascript"></block>
        <block type="new_tab"></block>
        <block type="switch_tab">
            <value name="INDEX">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
        </block>
        <block type="close_tab">
            <value name="INDEX">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
        </block>
    </category>
    <category name="HTML" colour="20">
        <block type="html_elements"></block>
        <block type="html_elements_by_selector"></block>
        <block type="wait_for_elements"></block>
        <block type="attribute_of"></block>
        <block type="text_of"></block>
        <block type="tag_name_of"></block>
        <block type="is_html_element_exist"></block>
        <block type="page_source"></block>
    </category>
    <category name="Events" colour="80">
        <block type="click"></block>
        <block type="type"></block>
        <block type="press_key"></block>
        <block type="wait_for"></block>
        <block type="scroll"></block>
    </category>

    <sep></sep>

    <category id="catLogic" colour="210" name="Logic">
        <block type="controls_if"></block>
        <block type="logic_compare"></block>
        <block type="logic_operation"></block>
        <block type="logic_negate"></block>
        <block type="logic_boolean"></block>
        <block type="logic_null"></block>
        <block type="logic_ternary"></block>
    </category>

    <category id="catLoops" colour="120" name="Loops">
        <block type="controls_repeat_ext">
            <value name="TIMES">
                <shadow type="math_number">
                    <field name="NUM">10</field>
                </shadow>
            </value>
        </block>
        <block type="controls_whileUntil"></block>
        <block type="controls_for">
            <value name="FROM">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
            <value name="TO">
                <shadow type="math_number">
                    <field name="NUM">10</field>
                </shadow>
            </value>
            <value name="BY">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
        </block>
        <block type="controls_forEach"></block>
        <block type="controls_flow_statements"></block>
    </category>


    <category id="catMath" colour="230" name="Math">
        <block type="math_number"></block>
        <block type="math_arithmetic">
            <value name="A">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
            <value name="B">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
        </block>
        <block type="math_single">
            <value name="NUM">
                <shadow type="math_number">
                    <field name="NUM">9</field>
                </shadow>
            </value>
        </block>
        <block type="math_trig">
            <value name="NUM">
                <shadow type="math_number">
                    <field name="NUM">45</field>
                </shadow>
            </value>
        </block>
        <block type="math_constant"></block>
        <block type="math_number_property">
            <value name="NUMBER_TO_CHECK">
                <shadow type="math_number">
                    <field name="NUM">0</field>
                </shadow>
            </value>
        </block>
        <block type="convert_text_to_number">
            <value name="TEXT">
                <shadow type="text">
                    <field name="TEXT">1</field>
                </shadow>
            </value>
        </block>
        <block type="math_change">
            <value name="DELTA">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
        </block>
        <block type="math_round">
            <value name="NUM">
                <shadow type="math_number">
                    <field name="NUM">3.1</field>
                </shadow>
            </value>
        </block>
        <block type="math_on_list"></block>
        <block type="math_modulo">
            <value name="DIVIDEND">
                <shadow type="math_number">
                    <field name="NUM">64</field>
                </shadow>
            </value>
            <value name="DIVISOR">
                <shadow type="math_number">
                    <field name="NUM">10</field>
                </shadow>
            </value>
        </block>
        <block type="math_constrain">
            <value name="VALUE">
                <shadow type="math_number">
                    <field name="NUM">50</field>
                </shadow>
            </value>
            <value name="LOW">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
            <value name="HIGH">
                <shadow type="math_number">
                    <field name="NUM">100</field>
                </shadow>
            </value>
        </block>
        <block type="math_random_int">
            <value name="FROM">
                <shadow type="math_number">
                    <field name="NUM">1</field>
                </shadow>
            </value>
            <value name="TO">
                <shadow type="math_number">
                    <field name="NUM">100</field>
                </shadow>
            </value>
        </block>
        <block type="math_random_float"></block>
    </category>


    <category id="catText" colour="160" name="Text">
        <block type="text"></block>
        <block type="text_join"></block>
        <block type="text_append">
            <value name="TEXT">
                <shadow type="text"></shadow>
            </value>
        </block>
        <block type="text_length">
            <value name="VALUE">
                <shadow type="text">
                    <field name="TEXT">abc</field>
                </shadow>
            </value>
        </block>
        <block type="text_isEmpty">
            <value name="VALUE">
                <shadow type="text">
                    <field name="TEXT"></field>
                </shadow>
            </value>
        </block>
        <block type="convert_to_text"></block>
        <block type="text_indexOf">
            <value name="FIND">
                <shadow type="text">
                    <field name="TEXT">abc</field>
                </shadow>
            </value>
        </block>
        <block type="text_charAt">

        </block>
        <block type="text_getSubstring">

        </block>
        <block type="text_changeCase">
            <value name="TEXT">
                <shadow type="text">
                    <field name="TEXT">abc</field>
                </shadow>
            </value>
        </block>
        <block type="text_trim">
            <value name="TEXT">
                <shadow type="text">
                    <field name="TEXT">abc</field>
                </shadow>
            </value>
        </block>
        <block type="text_print">
            <value name="TEXT">
                <shadow type="text">
                    <field name="TEXT">abc</field>
                </shadow>
            </value>
        </block>
        <block type="prompt">
            <value name="MESSAGE">
                <shadow type="text">
                    <field name="TEXT">Enter a URL</field>
                </shadow>
            </value>
            <value name="VALUE">
                <shadow type="text">
                    <field name="TEXT"></field>
                </shadow>
            </value>
        </block>
    </category>



    <category id="catLists" colour="260" name="Lists">
        <block type="lists_create_with">
            <mutation items="0"></mutation>
        </block>
        <block type="lists_create_with"></block>
        <block type="lists_chooser"></block>
        <block type="lists_repeat">
            <value name="NUM">
                <shadow type="math_number">
                    <field name="NUM">5</field>
                </shadow>
            </value>
        </block>
        <block type="lists_length"></block>
        <block type="lists_isEmpty"></block>
        <block type="lists_indexOf">
            
        </block>
        <block type="lists_getIndex">
            
        </block>
        <block type="lists_setIndex">
        </block>
        <block type="lists_getSublist">
        </block>
        <block type="lists_split">
            <value name="DELIM">
                <shadow type="text">
                    <field name="TEXT">,</field>
                </shadow>
            </value>
        </block>
        <block type="lists_regex">
            <value name="TEXT">
                <shadow type="text">
                    <field name="TEXT">hello, world</field>
                </shadow>
            </value>
        </block>
        <block type="lists_sort"></block>
    </category>

    <category id="catDict" colour="#2C5D6F" name="Dict">
        <block type="dict_create_with">
            <mutation items="0"></mutation>
        </block>
        <block type="dict_create_with"></block>
        <block type="dict_pair">
            <value name="KEY">
                <shadow type="text">
                    <field name="TEXT">key</field>
                </shadow>
            </value>
        </block>
        <block type="dict_get_value">
            <value name="KEY">
                <shadow type="text">
                    <field name="TEXT">key</field>
                </shadow>
            </value>
        </block>
        <block type="dict_set_value">
            <value name="KEY">
                <shadow type="text">
                    <field name="TEXT">key</field>
                </shadow>
            </value>
        </block>
        <block type="dict_get_all_keys"></block>
        <block type="dict_key_exist">
            <value name="KEY">
                <shadow type="text">
                    <field name="TEXT">key</field>
                </shadow>
            </value>
        </block>
    </category>
    
    <sep></sep>
    
    <category id="catVariables" colour="330" custom="VARIABLE" name="Variables"></category>
    <category id="catFunctions" colour="290" custom="PROCEDURE" name="Functions"></category>

    <sep></sep>

    <category id="catUtils" colour="#e67e22" name="Utils">
        <block type="utils_tryCatch"></block>
    </category>
</xml>
`;


/* <category id="catDict" colour="#2C5D6F" name="Dict">
    <block type="vector_constructor"></block>
</category> */


// <category name="File I/O" colour="185"></category>


/* <value name="VALUE">
    <block type="variables_get">
        <field name="VAR">list</field>
    </block>
</value> */