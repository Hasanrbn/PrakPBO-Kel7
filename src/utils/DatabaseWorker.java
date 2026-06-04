package utils;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * DatabaseWorker — Generic SwingWorker utility untuk menjalankan
 * operasi database di background thread agar EDT (Event Dispatch Thread)
 * tidak terblokir / UI tidak freeze.
 *
 * Cara pakai:
 *   new DatabaseWorker<List<Siswa>>(
 *       () -> siswaDAO.getAll(),          // Task DB (background thread)
 *       result -> tampilkanData(result),  // Callback sukses (EDT)
 *       err -> showError(err.getMessage())// Callback error (EDT)
 *   ).execute();
 */
public class DatabaseWorker<T> extends SwingWorker<T, Void> {

    private final Callable<T> task;
    private final Consumer<T> onSuccess;
    private final Consumer<Exception> onError;

    public DatabaseWorker(Callable<T> task,
                          Consumer<T> onSuccess,
                          Consumer<Exception> onError) {
        this.task = task;
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    protected T doInBackground() throws Exception {
        // Berjalan di background thread — aman untuk operasi DB
        return task.call();
    }

    @Override
    protected void done() {
        // Berjalan di EDT — aman untuk update UI
        try {
            T result = get();
            if (onSuccess != null) onSuccess.accept(result);
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (onError != null) onError.accept(new Exception(cause));
        }
    }
}
