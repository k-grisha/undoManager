package impl;

import org.junit.Assert;
import org.junit.Test;
import undo.Change;
import undo.Document;
import undo.UndoManager;

public class UndoManagerImplTest {

	private UndoManagerFactoryImpl undoManagerFactory = new UndoManagerFactoryImpl();
	private DocumentMock document = new DocumentMock();
	
	@Test
	public void baseTest() {
		UndoManager undoManager = undoManagerFactory.createUndoManager(document, 3);
		Assert.assertFalse(undoManager.canRedo());
		Assert.assertFalse(undoManager.canUndo());
		undoManager.registerChange(new ChangeMock("A"));
		undoManager.registerChange(new ChangeMock("B"));
		undoManager.registerChange(new ChangeMock("C"));
		undoManager.registerChange(new ChangeMock("D"));
		undoManager.registerChange(new ChangeMock("E"));
		Assert.assertEquals("ABCDE", document.getText());
		Assert.assertFalse(undoManager.canRedo());
		Assert.assertTrue(undoManager.canUndo());

		undoManager.undo();
		undoManager.undo();
		undoManager.undo();
		undoManager.undo();
		Assert.assertEquals("AB", document.getText());
		Assert.assertTrue(undoManager.canRedo());
		Assert.assertFalse(undoManager.canUndo());

		undoManager.redo();
		Assert.assertEquals("ABC", document.getText());
		Assert.assertTrue(undoManager.canRedo());

		undoManager.registerChange(new ChangeMock("X"));
		Assert.assertEquals("ABCX", document.getText());
		Assert.assertFalse(undoManager.canRedo());
	}


	class ChangeMock implements Change {
		String change;

		ChangeMock(String change) {
			this.change = change;
		}

		public void apply(Document doc) {
			doc.insert(0, change);
		}

		public void revert(Document doc) {
			doc.delete(0, change);
		}
	}

	class DocumentMock implements Document {
		private String text = "";

		public String getText() {
			return text;
		}

		public void delete(int pos, String s) {
			text = text.substring(0, text.lastIndexOf(s));
		}

		public void insert(int pos, String s) {
			text = text.concat(s);
		}

		public void setDot(int pos) {

		}
	}

}