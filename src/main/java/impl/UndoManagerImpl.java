package impl;

import undo.Change;
import undo.Document;
import undo.UndoManager;

import java.util.LinkedList;

/**
 * Индекс следующего изменеия это либо размер текущего списка или, если undo было выполнено,
 * указывает на индекс последнего изменения которое было вызвано.
 */

public class UndoManagerImpl implements UndoManager {

	private final LinkedList<Change> changes = new LinkedList<Change>();
	private final Document doc;
	private final int bufferSize;
	private int currentIndex = 0;


	public UndoManagerImpl(Document doc, int bufferSize) {
		this.doc = doc;
		this.bufferSize = bufferSize;
	}

	public void registerChange(Change change) {
		// remove all irrelevant changes
		while (currentIndex < changes.size()) {
			changes.removeFirst();
		}
		// replace the oldest change if necessary
		if (changes.size() >= bufferSize) {
			changes.removeFirst();
		}
		// apply new change
		change.apply(doc);
		changes.add(change);
		currentIndex = changes.size();
	}

	public boolean canUndo() {
		return currentIndex > 0;
	}

	public void undo() {
		if (!canUndo()) {
			return;
		}
		currentIndex--;
		changes.get(currentIndex).revert(doc);
	}

	public boolean canRedo() {
		return currentIndex < changes.size();
	}

	public void redo() {
		if (!canRedo()) {
			return;
		}
		changes.get(currentIndex).apply(doc);
		currentIndex++;
	}
}
