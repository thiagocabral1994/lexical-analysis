package br.ufjf.service;

import br.ufjf.model.*;

import javax.management.BadAttributeValueExpException;
import javax.xml.parsers.DocumentBuilderFactory;

import br.ufjf.structure.Group;
import org.w3c.dom.Element;

public abstract class XmlReader {
    @SuppressWarnings("unchecked")
    public static DFA xmlToDFA(String filePath) throws Exception {
        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        var documentBuilder = documentBuilderFactory.newDocumentBuilder();
        var document = documentBuilder.parse(filePath);

        var element = document.getDocumentElement();
        var symbolsNode = element.getElementsByTagName("symbols");
        var statesNode = element.getElementsByTagName("states");
        var endStatesNode = element.getElementsByTagName("endStatesStar");
        var programFunctionNode = element.getElementsByTagName("programFunction");
        var startStateNode = element.getElementsByTagName("startState");

        var symbolGroup = XmlReader.getChildGroupElements(Symbol.class, (Element) symbolsNode.item(0), "value");
        var stateGroup = XmlReader.getChildGroupElements(State.class, (Element) statesNode.item(0), "value");
        var endStateGroup = XmlReader.getChildGroupElements(State.class, (Element) endStatesNode.item(0), "value");
        var programFunction = XmlReader.getChildGroupElements(TransitionD.class, (Element) programFunctionNode.item(0), "start", "finish", "symbol");

        var startStateElement = (Element) startStateNode.item(0);
        var initialState = new State(startStateElement.getAttribute("value"));

        return new DFA(
                symbolGroup,
                stateGroup,
                programFunction,
                initialState,
                endStateGroup
        );
    }

    @SuppressWarnings("unchecked")
    public static NFA xmlToNFA(String filePath) throws Exception {
        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        var documentBuilder = documentBuilderFactory.newDocumentBuilder();
        var document = documentBuilder.parse(filePath);

        var element = document.getDocumentElement();
        var symbolsNode = element.getElementsByTagName("symbols");
        var statesNode = element.getElementsByTagName("states");
        var endStatesNode = element.getElementsByTagName("endStatesStar");
        var programFunctionNode = element.getElementsByTagName("programFunction");
        var startStateNode = element.getElementsByTagName("startState");

        var symbolGroup = XmlReader.getChildGroupElements(Symbol.class, (Element) symbolsNode.item(0), "value");
        var stateGroup = XmlReader.getChildGroupElements(State.class, (Element) statesNode.item(0), "value");
        var endStateGroup = XmlReader.getChildGroupElements(State.class, (Element) endStatesNode.item(0), "value");
        var programFunction = XmlReader.getChildGroupElements(TransitionN.class, (Element) programFunctionNode.item(0), "start", "finish", "symbol");

        var startStateElement = (Element) startStateNode.item(0);
        var initialState = new State(startStateElement.getAttribute("value"));

        return new NFA(
                symbolGroup,
                stateGroup,
                programFunction,
                initialState,
                endStateGroup
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T extends Base<T>> Group getChildGroupElements(Class<T> clazz, Element element, String ...params) throws Exception {
        if(element == null) {
            throw new BadAttributeValueExpException("Element does not exist in DFA (null)");
        }

        var children = element.getElementsByTagName("element");

        if(children == null) {
            throw new BadAttributeValueExpException("Children node list does not exist in DFA (null)");
        }

        var group = new Group();
        for (int i = 0; i < children.getLength(); i++) {
            var child = (Element) children.item(i);

            if (child == null) {
                throw new BadAttributeValueExpException("Child node does not exist in DFA (null)");
            }

            var arguments = new String[params.length];
            for (int j = 0; j < params.length; j++) {
                arguments[j] = child.getAttribute(params[j]);
            }

            switch (clazz.getSimpleName()) {
                case "Symbol" -> group.insert(new Symbol(arguments[0]));
                case "State" -> group.insert(new State(arguments[0]));
                case "TransitionD" -> {
                    var start = new State(arguments[0]);
                    var finish = new State(arguments[1]);
                    var symbol = new Symbol(arguments[2]);
                    group.insert(new TransitionD(start, finish, symbol));
                }
                case "TransitionN" -> {
                    var transitionExists = false;
                    var start = new State(arguments[0]);
                    var finish = new State(arguments[1]);
                    var symbol = new Symbol(arguments[2]);
                    for (var el : group.getElements()) {
                        var transitionN = (TransitionN) el;
                        if (
                                start.equals(transitionN.getStart()) &&
                                symbol.equals(transitionN.getSymbol())
                        ){
                            var finishGroup = transitionN.getFinish();
                            finishGroup.insert(finish);
                            transitionN.setFinish(finishGroup);
                            transitionExists = true;
                        }
                    }

                    if (transitionExists) {
                        break;
                    }
                    var groupFinish = new Group<State>();
                    groupFinish.insert(finish);
                    group.insert(new TransitionN(start, groupFinish, symbol));
                }
                default -> throw new BadAttributeValueExpException("Class not mapped in method");
            }
        }
        return group;
    }
}
