package net.aveyon.intermediate_solidity_extractor

import net.aveyon.intermediate_solidity.ContractConcepts
import net.aveyon.intermediate_solidity.Enumeration
import net.aveyon.intermediate_solidity.Error
import net.aveyon.intermediate_solidity.Event
import net.aveyon.intermediate_solidity.Field
import net.aveyon.intermediate_solidity.Function
import net.aveyon.intermediate_solidity.FunctionParameter
import net.aveyon.intermediate_solidity.GeneralSolidityConcepts
import net.aveyon.intermediate_solidity.ImportedConcept
import net.aveyon.intermediate_solidity.Interface
import net.aveyon.intermediate_solidity.LocalField
import net.aveyon.intermediate_solidity.Modifier
import net.aveyon.intermediate_solidity.SmartContract
import net.aveyon.intermediate_solidity.SmartContractModel
import net.aveyon.intermediate_solidity.SolidityConcepts
import net.aveyon.intermediate_solidity.Structure
import net.aveyon.intermediate_solidity.impl.ExpressionIfImpl
import net.aveyon.intermediate_solidity.ExpressionString
import net.aveyon.intermediate_solidity.ExpressionIf
import net.aveyon.intermediate_solidity.Constructor

/**
 * Class for generating concrete Solidity code by a given {@link SmartContractModel}
 * 
 * Take care when formating the code since it can break the idention of the generated code
 */
class IntermediateSolidityExtractor {

	def String generateSmartContractModel(SmartContractModel model) {
		return '''
			// SPDX-License-Identifier: «model.license»
			pragma solidity «model.pragma»;

			«FOR i: model.imports»«generateImports(i)»«ENDFOR»

			«generateSolidityConcepts(model.definitions)»
		'''
	}
	
	def String generateSolidityConcepts(SolidityConcepts definitions) {
		return '''
			«generateGeneralSolidityConcepts(definitions)»
			
			«FOR iface : definitions.interfaces»
				«generateInterface(iface)»
			«ENDFOR»
			«FOR c : definitions.contracts BEFORE "\n"»
				«generateContract(c)»
			«ENDFOR»
		'''
	}

	def String generateGeneralSolidityConcepts(GeneralSolidityConcepts definitions) {
		return '''
			«FOR e : definitions.enums»
				«generateEnumeration(e)»
			«ENDFOR»
			«FOR error : definitions.errors BEFORE "\n"»
				«generateError(error)»
			«ENDFOR»
			«FOR event : definitions.events BEFORE "\n"»
				«generateEvent(event)»
			«ENDFOR»
			«FOR struct : definitions.structures BEFORE "\n"»
				«generateStructure(struct)»
			«ENDFOR»
			«FOR fun : definitions.functions BEFORE "\n"»
				«generateFunction(fun)»
			«ENDFOR»
		'''
	}
	
	def String generateContractConcepts(ContractConcepts definitions) {
		return '''«IF definitions.constructor != null»«generateConstructor(definitions.constructor)»«ENDIF»'''
		+
		'''«generateGeneralSolidityConcepts(definitions)»'''
		+'''
			«FOR mod : definitions.modifiers»
				«generateModifier(mod)»
			«ENDFOR»
		'''
	}

	def String generateImports(ImportedConcept importedConcept) {
		return '''
			import "«importedConcept.path»";
		'''
	}

	def String generateInterface(Interface iface) {
		return '''
			interface «iface.name» «IF iface.extends.size > 0»is «FOR ext: iface.extends»«ext.name»«ENDFOR»«ENDIF» {
				«generateGeneralSolidityConcepts(iface.definitions)»
			}
		'''
	}

	def String generateContract(SmartContract contract) {
		return '''
			«IF contract.isAbstract»abstract «ENDIF»contract «contract.name» «Util.printExtension(contract)» {
				«FOR f: contract.fields»
					«generateField(f)»;
				«ENDFOR»
				«generateContractConcepts(contract.definitions)»
			}
		'''
	}

	def String generateField(Field field) {
		return '''«Util.printFieldKeyWords(field)» «field.name»«IF field.value != null» = «field.value»«ENDIF»'''
	}

	def String generateStructure(Structure struct) {
		return '''
			struct «struct.name» {
				«FOR p: struct.fields»
					«generateLocalField(p)»;
				«ENDFOR»
			}
		'''
	}

	def String generateError(Error error) {
		return '''
			error «error.name»(«FOR p: error.errorParams SEPARATOR ", "»«generateLocalField(p)»«ENDFOR»);
		'''
	}

	def String generateEvent(Event event) {
		return '''
			event «event.name»(«FOR p: event.eventParams SEPARATOR ", "»«generateLocalField(p)»«ENDFOR»);
		'''
	}

	def String generateFunction(Function function) {
		return '''
			«IF function.isAbstract»abstract «ENDIF»function «function.name»«IF function.parameters.length == 0»()«ELSE»(
				«FOR p: function.parameters SEPARATOR ","»
					«generateFunctionParameter(p)»
				«ENDFOR»
			)«ENDIF» «Util.printFunctionKeyWords(function)» «Util.printReturnValues(function.returns)»'''.toString().trim()
		+ 
		'''«IF function.expressions.length == 0 || function.isAbstract»;«ELSE» {
	«FOR exp : function.expressions»«generateExpression(exp)»«ENDFOR»
}
			«ENDIF»
		'''
	}
	
	def dispatch String generateExpression(ExpressionIf exp) {
		return '''
			«FOR c: exp.conditions»
				«c.first.toString» {
					«FOR e: c.second»
						«generateExpression(e)»
					«ENDFOR»
				}
			«ENDFOR»
		'''
	}

	def dispatch String generateExpression(ExpressionString exp) {
		return '''«exp.value.toString»«IF !exp.value.startsWith("//")»;«ENDIF»
		'''
	}

	def String generateFunctionParameter(FunctionParameter param) {
		return '''
			«param.type» «Util.printFunctionParameterKeyWords(param)» «param.name»
		'''
	}

	def String generateEnumeration(Enumeration enumeration) {
		return '''
			enum «enumeration.name» {
				«FOR v: enumeration.values SEPARATOR ","»
					«v»
				«ENDFOR»
			}
		'''
	}
	
	def String generateConstructor(Constructor ctor) {
		if (ctor.expressions.size == 0) return ""
		
		return '''
			constructor() «Util.printConstructorKeyWords(ctor)» {
				«FOR exp : ctor.expressions»«generateExpression(exp)»«ENDFOR»
			}
		'''
	}

	def String generateModifier(Modifier modifier) {
		return '''
			modifier «modifier.name» «Util.printModifierKeyWords(modifier)»{
				«FOR e: modifier.expressions»«generateExpression(e)»«ENDFOR»
			}
		'''
	}

	def String generateLocalField(LocalField localField) {
		return '''«Util.printLocalFieldKeyWords(localField)» «localField.name»'''
	}
	


}
