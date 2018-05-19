package impl;

import undo.Document;
import undo.UndoManager;
import undo.UndoManagerFactory;

public class UndoManagerFactoryImpl implements UndoManagerFactory {
	public UndoManager createUndoManager(Document doc, int bufferSize) {
		return new UndoManagerImpl(doc, bufferSize);
	}
}
