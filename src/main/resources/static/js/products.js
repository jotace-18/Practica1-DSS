const deleteDialog = document.getElementById('delete-dialog');
let pendingDeleteForm = null;
let confirmedDeleteForm = null;

function askToDelete(form) {
    pendingDeleteForm = form;
    document.getElementById('delete-product-name').textContent = form.dataset.productName;
    deleteDialog.showModal();
}

document.querySelectorAll('.delete-product').forEach(form => {
    form.querySelector('.delete-trigger').addEventListener('click', () => askToDelete(form));
    form.addEventListener('submit', event => {
        if (confirmedDeleteForm !== form) {
            event.preventDefault();
            askToDelete(form);
        }
    });
});

document.getElementById('delete-cancel').addEventListener('click', () => deleteDialog.close());
document.getElementById('delete-confirm').addEventListener('click', () => {
    if (!pendingDeleteForm) return;
    confirmedDeleteForm = pendingDeleteForm;
    confirmedDeleteForm.requestSubmit();
    deleteDialog.close();
});
deleteDialog.addEventListener('close', () => {
    pendingDeleteForm = null;
    confirmedDeleteForm = null;
});
