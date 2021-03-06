package net.aveyon.intermediate_solidity_extractor;

import com.google.common.base.Objects;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.aveyon.intermediate_solidity.Constructor;
import net.aveyon.intermediate_solidity.ContractConcepts;
import net.aveyon.intermediate_solidity.Enumeration;
import net.aveyon.intermediate_solidity.Event;
import net.aveyon.intermediate_solidity.Expression;
import net.aveyon.intermediate_solidity.Field;
import net.aveyon.intermediate_solidity.Function;
import net.aveyon.intermediate_solidity.FunctionParameter;
import net.aveyon.intermediate_solidity.GeneralSolidityConcepts;
import net.aveyon.intermediate_solidity.ImportedConcept;
import net.aveyon.intermediate_solidity.Interface;
import net.aveyon.intermediate_solidity.LocalField;
import net.aveyon.intermediate_solidity.Modifier;
import net.aveyon.intermediate_solidity.SmartContract;
import net.aveyon.intermediate_solidity.SmartContractModel;
import net.aveyon.intermediate_solidity.SolidityConcepts;
import net.aveyon.intermediate_solidity.Statement;
import net.aveyon.intermediate_solidity.StatementExpression;
import net.aveyon.intermediate_solidity.StatementIf;
import net.aveyon.intermediate_solidity.Structure;
import net.aveyon.intermediate_solidity.Type;
import net.aveyon.intermediate_solidity.util.Pair;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;

/**
 * Class for generating concrete Solidity code by a given {@link SmartContractModel}
 * 
 * Take care when formating the code since it can break the idention of the generated code
 */
@SuppressWarnings("all")
public class IntermediateSolidityExtractor {
  public String generateSmartContractModel(final SmartContractModel model) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// SPDX-License-Identifier: ");
    String _license = model.getLicense();
    _builder.append(_license);
    _builder.newLineIfNotEmpty();
    _builder.append("pragma solidity ");
    String _pragma = model.getPragma();
    _builder.append(_pragma);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      List<ImportedConcept> _imports = model.getImports();
      for(final ImportedConcept i : _imports) {
        String _generateImports = this.generateImports(i);
        _builder.append(_generateImports);
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    String _generateSolidityConcepts = this.generateSolidityConcepts(model.getDefinitions());
    _builder.append(_generateSolidityConcepts);
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String generateSolidityConcepts(final SolidityConcepts definitions) {
    StringConcatenation _builder = new StringConcatenation();
    String _generateGeneralSolidityConcepts = this.generateGeneralSolidityConcepts(definitions);
    _builder.append(_generateGeneralSolidityConcepts);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      List<Interface> _interfaces = definitions.getInterfaces();
      for(final Interface iface : _interfaces) {
        String _generateInterface = this.generateInterface(iface);
        _builder.append(_generateInterface);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<SmartContract> _contracts = definitions.getContracts();
      boolean _hasElements = false;
      for(final SmartContract c : _contracts) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("\n");
        }
        String _generateContract = this.generateContract(c);
        _builder.append(_generateContract);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  public String generateGeneralSolidityConcepts(final GeneralSolidityConcepts definitions) {
    StringConcatenation _builder = new StringConcatenation();
    {
      List<Enumeration> _enums = definitions.getEnums();
      for(final Enumeration e : _enums) {
        String _generateEnumeration = this.generateEnumeration(e);
        _builder.append(_generateEnumeration);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<net.aveyon.intermediate_solidity.Error> _errors = definitions.getErrors();
      boolean _hasElements = false;
      for(final net.aveyon.intermediate_solidity.Error error : _errors) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("\n");
        }
        String _generateError = this.generateError(error);
        _builder.append(_generateError);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<Event> _events = definitions.getEvents();
      boolean _hasElements_1 = false;
      for(final Event event : _events) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
          _builder.append("\n");
        }
        String _generateEvent = this.generateEvent(event);
        _builder.append(_generateEvent);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<Structure> _structures = definitions.getStructures();
      boolean _hasElements_2 = false;
      for(final Structure struct : _structures) {
        if (!_hasElements_2) {
          _hasElements_2 = true;
          _builder.append("\n");
        }
        String _generateStructure = this.generateStructure(struct);
        _builder.append(_generateStructure);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<Function> _functions = definitions.getFunctions();
      boolean _hasElements_3 = false;
      for(final Function fun : _functions) {
        if (!_hasElements_3) {
          _hasElements_3 = true;
          _builder.append("\n");
        }
        String _generateFunction = this.generateFunction(fun);
        _builder.append(_generateFunction);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  public String generateContractConcepts(final ContractConcepts definitions) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Constructor _constructor = definitions.getConstructor();
      boolean _notEquals = (!Objects.equal(_constructor, null));
      if (_notEquals) {
        String _generateConstructor = this.generateConstructor(definitions.getConstructor());
        _builder.append(_generateConstructor);
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    String _generateGeneralSolidityConcepts = this.generateGeneralSolidityConcepts(definitions);
    _builder_1.append(_generateGeneralSolidityConcepts);
    String _plus = (_builder.toString() + _builder_1);
    StringConcatenation _builder_2 = new StringConcatenation();
    {
      List<Modifier> _modifiers = definitions.getModifiers();
      for(final Modifier mod : _modifiers) {
        String _generateModifier = this.generateModifier(mod);
        _builder_2.append(_generateModifier);
        _builder_2.newLineIfNotEmpty();
      }
    }
    return (_plus + _builder_2);
  }
  
  public String generateImports(final ImportedConcept importedConcept) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import \"");
    String _path = importedConcept.getPath();
    _builder.append(_path);
    _builder.append("\";");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String generateInterface(final Interface iface) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("interface ");
    String _name = iface.getName();
    _builder.append(_name);
    _builder.append(" ");
    {
      int _size = iface.getExtends().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("is ");
        {
          LinkedList<Interface> _extends = iface.getExtends();
          for(final Interface ext : _extends) {
            String _name_1 = ext.getName();
            _builder.append(_name_1);
          }
        }
      }
    }
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _generateGeneralSolidityConcepts = this.generateGeneralSolidityConcepts(iface.getDefinitions());
    _builder.append(_generateGeneralSolidityConcepts, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String generateContract(final SmartContract contract) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isAbstract = contract.isAbstract();
      if (_isAbstract) {
        _builder.append("abstract ");
      }
    }
    _builder.append("contract ");
    String _name = contract.getName();
    _builder.append(_name);
    _builder.append(" ");
    String _printExtension = Util.printExtension(contract);
    _builder.append(_printExtension);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      List<Field> _fields = contract.getFields();
      for(final Field f : _fields) {
        _builder.append("\t");
        String _generateField = this.generateField(f);
        _builder.append(_generateField, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    String _generateContractConcepts = this.generateContractConcepts(contract.getDefinitions());
    _builder.append(_generateContractConcepts, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String generateField(final Field field) {
    StringConcatenation _builder = new StringConcatenation();
    String _printFieldKeyWords = Util.printFieldKeyWords(field);
    _builder.append(_printFieldKeyWords);
    _builder.append(" ");
    String _name = field.getName();
    _builder.append(_name);
    {
      String _value = field.getValue();
      boolean _notEquals = (!Objects.equal(_value, null));
      if (_notEquals) {
        _builder.append(" = ");
        String _value_1 = field.getValue();
        _builder.append(_value_1);
      }
    }
    return _builder.toString();
  }
  
  public String generateStructure(final Structure struct) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("struct ");
    String _name = struct.getName();
    _builder.append(_name);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      List<LocalField> _fields = struct.getFields();
      for(final LocalField p : _fields) {
        _builder.append("\t");
        String _generateLocalField = this.generateLocalField(p);
        _builder.append(_generateLocalField, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String generateError(final net.aveyon.intermediate_solidity.Error error) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("error ");
    String _name = error.getName();
    _builder.append(_name);
    _builder.append("(");
    {
      List<LocalField> _errorParams = error.getErrorParams();
      boolean _hasElements = false;
      for(final LocalField p : _errorParams) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        String _generateLocalField = this.generateLocalField(p);
        _builder.append(_generateLocalField);
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String generateEvent(final Event event) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("event ");
    String _name = event.getName();
    _builder.append(_name);
    _builder.append("(");
    {
      List<LocalField> _eventParams = event.getEventParams();
      boolean _hasElements = false;
      for(final LocalField p : _eventParams) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        String _generateLocalField = this.generateLocalField(p);
        _builder.append(_generateLocalField);
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String generateFunction(final Function function) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isAbstract = function.isAbstract();
      if (_isAbstract) {
        _builder.append("abstract ");
      }
    }
    _builder.append("function ");
    String _name = function.getName();
    _builder.append(_name);
    {
      int _length = ((Object[])Conversions.unwrapArray(function.getParameters(), Object.class)).length;
      boolean _equals = (_length == 0);
      if (_equals) {
        _builder.append("()");
      } else {
        _builder.append("(");
        _builder.newLineIfNotEmpty();
        {
          List<FunctionParameter> _parameters = function.getParameters();
          boolean _hasElements = false;
          for(final FunctionParameter p : _parameters) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "\t");
            }
            _builder.append("\t");
            String _generateFunctionParameter = this.generateFunctionParameter(p);
            _builder.append(_generateFunctionParameter, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append(")");
      }
    }
    _builder.append(" ");
    String _printFunctionKeyWords = Util.printFunctionKeyWords(function);
    _builder.append(_printFunctionKeyWords);
    _builder.append(" ");
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(function.getReturns(), Object.class)).length;
      boolean _greaterThan = (_length_1 > 0);
      if (_greaterThan) {
        _builder.append(" returns (");
        _builder.newLineIfNotEmpty();
        {
          List<FunctionParameter> _returns = function.getReturns();
          boolean _hasElements_1 = false;
          for(final FunctionParameter p_1 : _returns) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate(",", "\t");
            }
            _builder.append("\t");
            String _generateFunctionParameter_1 = this.generateFunctionParameter(p_1);
            _builder.append(_generateFunctionParameter_1, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append(")");
      }
    }
    String _trim = _builder.toString().trim();
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      if (((((Object[])Conversions.unwrapArray(function.getStatements(), Object.class)).length == 0) || function.isAbstract())) {
        _builder_1.append(";");
      } else {
        _builder_1.append(" {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t");
        {
          List<Statement> _statements = function.getStatements();
          for(final Statement s : _statements) {
            String _generateStatement = this.generateStatement(s);
            _builder_1.append(_generateStatement, "\t");
          }
        }
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("}");
        _builder_1.newLine();
      }
    }
    return (_trim + _builder_1);
  }
  
  protected String _generateStatement(final StatementIf statement) {
    StringConcatenation _builder = new StringConcatenation();
    {
      List<Pair<Expression, List<Statement>>> _conditions = statement.getConditions();
      for(final Pair<Expression, List<Statement>> c : _conditions) {
        String _string = c.getFirst().toString();
        _builder.append(_string);
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        {
          List<Statement> _second = c.getSecond();
          for(final Statement s : _second) {
            _builder.append("\t");
            String _generateStatement = this.generateStatement(s);
            _builder.append(_generateStatement, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder.toString();
  }
  
  protected String _generateStatement(final StatementExpression statement) {
    StringConcatenation _builder = new StringConcatenation();
    String _string = statement.getExpr().getValue().toString();
    _builder.append(_string);
    {
      boolean _startsWith = statement.getExpr().getValue().startsWith("//");
      boolean _not = (!_startsWith);
      if (_not) {
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String generateFunctionParameter(final FunctionParameter param) {
    StringConcatenation _builder = new StringConcatenation();
    String _generateType = this.generateType(param.getType());
    _builder.append(_generateType);
    _builder.append(" ");
    String _printFunctionParameterKeyWords = Util.printFunctionParameterKeyWords(param);
    _builder.append(_printFunctionParameterKeyWords);
    _builder.append(" ");
    String _name = param.getName();
    _builder.append(_name);
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String generateEnumeration(final Enumeration enumeration) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("enum ");
    String _name = enumeration.getName();
    _builder.append(_name);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      List<String> _values = enumeration.getValues();
      boolean _hasElements = false;
      for(final String v : _values) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        _builder.append(v, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String generateConstructor(final Constructor ctor) {
    int _size = ctor.getStatements().size();
    boolean _equals = (_size == 0);
    if (_equals) {
      return "";
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("constructor() ");
    String _printConstructorKeyWords = Util.printConstructorKeyWords(ctor);
    _builder.append(_printConstructorKeyWords);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    {
      List<Statement> _statements = ctor.getStatements();
      for(final Statement s : _statements) {
        String _generateStatement = this.generateStatement(s);
        _builder.append(_generateStatement, "\t");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String generateType(final Type type) {
    return type.getName();
  }
  
  public String generateModifier(final Modifier modifier) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("modifier ");
    String _name = modifier.getName();
    _builder.append(_name);
    _builder.append(" ");
    String _printModifierKeyWords = Util.printModifierKeyWords(modifier);
    _builder.append(_printModifierKeyWords);
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    {
      List<Statement> _statements = modifier.getStatements();
      for(final Statement s : _statements) {
        String _generateStatement = this.generateStatement(s);
        _builder.append(_generateStatement, "\t");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String generateLocalField(final LocalField localField) {
    StringConcatenation _builder = new StringConcatenation();
    String _printLocalFieldKeyWords = Util.printLocalFieldKeyWords(localField);
    _builder.append(_printLocalFieldKeyWords);
    _builder.append(" ");
    String _name = localField.getName();
    _builder.append(_name);
    return _builder.toString();
  }
  
  public String generateStatement(final Statement statement) {
    if (statement instanceof StatementExpression) {
      return _generateStatement((StatementExpression)statement);
    } else if (statement instanceof StatementIf) {
      return _generateStatement((StatementIf)statement);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(statement).toString());
    }
  }
}
