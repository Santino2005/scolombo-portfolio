import { api } from "./client";

export function findVisitorByDni(dni) {
    return api.get(`/visitor/${dni}`);
}

export function createVisitor(data) {
    const formData = new FormData();

    formData.append("dni", data.dni);
    formData.append("fullName", data.fullName);
    formData.append("company", data.company);
    formData.append("sector", data.sector);
    formData.append("photo", data.photo);

    return api.post("/visitor", formData);
}

export function countVisitors() {
    return api.get("/visitor/count");
}