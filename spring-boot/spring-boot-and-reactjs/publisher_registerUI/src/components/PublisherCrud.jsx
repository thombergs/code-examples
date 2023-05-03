import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import PublisherList from "./PublisherList";

const PublisherCrud = () => {
  /* state definition  */
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [draft, setDraft] = useState("");
  const [published, setPublished] = useState("");
  const [publishers, setPublishers] = useState([]);

  /* manage side effects */
  useEffect(() => {
    (async () => await Load())();
  }, []);

  /* being handlers */
  async function Load() {
    const result = await api.get("/all");
    setPublishers(result.data);
  }

  async function save(event) {
    event.preventDefault();
    try {
      await api.post("/create", {
        name: name,
        email: email,
        draft: draft,
        published: published,
      });
      alert("Publisher Record Saved");
      // reset state
      setId("");
      setName("");
      setEmail("");
      setDraft("");
      setPublished("");
      Load();
    } catch (err) {
      alert("Publisher Record Not Saved");
    }
  }

  async function editEmployee(publishers) {
    setName(publishers.name);
    setEmail(publishers.email);
    setDraft(publishers.draft);
    setPublished(publishers.published);
    setId(publishers.id);
  }

  async function DeleteEmployee(id) {
    await api.delete("/delete/" + id);
    alert("Publisher Details Deleted Successfully");
    Load();
  }

  async function update(event) {
    event.preventDefault();
    try {
      if (!id) return alert("Publisher Details No Found");
      await api.put("/update", {
        id: id,
        name: name,
        email: email,
        draft: draft,
        published: published,
      });
      alert("Publisher Details Updated");
      // reset state
      setId("");
      setName("");
      setEmail("");
      setDraft("");
      setPublished("");
      Load();
    } catch (err) {
      alert(err);
    }
  }
  /* end handlers */

  /* jsx */
  return (
    <div className="container mt-4">
      <h1 className="text-center">List Of Publisher</h1>
      <form>
        <div className="form-group my-2">
          <input
            type="text"
            className="form-control"
            id="id"
            hidden
            value={id}
            onChange={event => {
              setId(event.target.value);
            }}
          />
          <label>Name</label>
          <input
            type="text"
            className="form-control"
            id="name"
            value={name}
            onChange={event => {
              setName(event.target.value);
            }}
          />
        </div>

        <div className="form-group mb-2">
          <label>Email</label>
          <input
            type="text"
            className="form-control"
            id="email"
            value={email}
            onChange={event => {
              setEmail(event.target.value);
            }}
          />
        </div>

        <div className="row">
          <div className="col-4">
            <label>Draft</label>
            <input
              type="text"
              className="form-control"
              id="draft"
              value={draft}
              placeholder="Post(s) in draft"
              onChange={event => {
                setDraft(event.target.value);
              }}
            />
          </div>
          <div className="col-4">
            <label>Published</label>
            <input
              type="text"
              className="form-control"
              id="published"
              value={published}
              placeholder="Published Post(s)"
              onChange={event => {
                setPublished(event.target.value);
              }}
            />
          </div>
        </div>

        <div>
          <button className="btn btn-primary m-4" onClick={save}>
            Register
          </button>
          <button className="btn btn-warning m-4" onClick={update}>
            Update
          </button>
        </div>
      </form>
      {/* passing child component */}
      <PublisherList
        publishers={publishers}
        editEmployee={editEmployee}
        DeleteEmployee={DeleteEmployee}
      />
    </div>
  );
};

export default PublisherCrud;
