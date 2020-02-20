export function prepareFormData(file: File, object: Blob): FormData {
  let formData = new FormData();
  formData.append("file", file);
  formData.append("properties", object);
  return formData;
}
