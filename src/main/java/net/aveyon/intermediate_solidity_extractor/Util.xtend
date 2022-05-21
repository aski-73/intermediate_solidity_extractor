package net.aveyon.intermediate_solidity_extractor

import net.aveyon.intermediate_solidity.Visibility
import java.util.List
import net.aveyon.intermediate_solidity.Function
import net.aveyon.intermediate_solidity.Field
import net.aveyon.intermediate_solidity.LocalField
import net.aveyon.intermediate_solidity.Modifier
import net.aveyon.intermediate_solidity.SmartContract
import net.aveyon.intermediate_solidity.Node
import java.util.stream.Stream
import java.util.Collections
import java.util.stream.Collectors

class Util {

	def static printFunctionKeyWords(Function function) {
		return '''
		«printVisibility(function.visibility)»''' 
		+ '''«IF function.doesOverride» override«ENDIF»''' 
		+ '''«IF function.virtual» virtual«ENDIF»''' 
		+ '''«IF function.payable» payable«ENDIF»'''
	}

	def static printModifierKeyWords(Modifier modifier) {
		return '''
		«IF modifier.doesOverride» override«ENDIF»''' + '''
		«IF modifier.virtual» virtual«ENDIF»'''
	}

	def static printFieldKeyWords(Field field) {
		return '''«printLocalFieldKeyWords(field)» «printVisibility(field.visibility)»'''
	}

	def static printLocalFieldKeyWords(LocalField field) {
		return '''«field.type»«IF field.payable» payable«ENDIF»'''
	}

	def static printVisibility(Visibility visibility) {
		return '''«IF visibility !== null»«visibility.name.toLowerCase()»«ELSE»public«ENDIF»'''
	}

	def static printReturnValues(List<String> returnValues) {
		if (returnValues === null || returnValues.size() == 0) {
			return ""
		}

		return '''«FOR r : returnValues SEPARATOR ", "»«r»«ENDFOR»'''
	}
	
	def static printExtension(SmartContract contract) {
		if (contract.extends.length == 0 && contract.implements.length == 0)
			return ""
			
		val List<Node> extensions = Stream.concat(contract.extends.stream(), contract.implements.stream()).collect(Collectors.toList())
		
		return '''is «FOR e: extensions SEPARATOR ", "»«e.name»«ENDFOR»'''
	}
}
