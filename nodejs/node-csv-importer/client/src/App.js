import React, { useMemo, useState, useEffect } from "react";
import axios from "axios";
import Table from "./Table";
import './App.css';

const Children = ({ values }) => {
  return (
    <>
      {values.map((child, idx) => {
        return (
          <div className="image">
            <img
              src={child.avatar}
              alt="Profile"
            />
            </div>
        );
      })}
    </>
  );
};

const Avatar = ({ value }) => {
  return (
    <div className="image">
      <img
        src={value}
        alt="Profile"
      />
      </div>
  );
};

const uploadToServer = (file, onUploadProgress) => {
  let formData = new FormData();

  formData.append("file", file);

  return axios.post('http://localhost:8080/api/csv/upload', formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
    onUploadProgress,
  });
};

function App() {

  const [data, setData] = useState([]);
  const [selectedFiles, setSelectedFiles] = useState(undefined);
  const [currentFile, setCurrentFile] = useState(undefined);
  const [progress, setProgress] = useState(0);
  const [message, setMessage] = useState("");

  const columns = useMemo(
    () => [
      {
        Header: "Employee Details",
        columns: [
          {
            Header: "Avatar",
            accessor: "avatar",
            Cell: ({ cell: { value } }) => <Avatar value={value} />
          },
          {
            Header: "Name",
            accessor: "name"
          },
          {
            Header: "Email",
            accessor: "email"
          },
          {
            Header: "Username",
            accessor: "username"
          },
          {
            Header: "DOB",
            accessor: "dob"
          },
          {
            Header: "Company",
            accessor: "company"
          },
          {
            Header: "Address",
            accessor: "address"
          },
          {
            Header: "Location",
            accessor: "location"
          },
          {
            Header: "Salary",
            accessor: "salary"
          },
          {
            Header: "Role",
            accessor: "role"
          },
          {
            Header: "Direct Reportee",
            accessor: "children",
            Cell: ({ cell: { value } }) => <Children values={value} />
          }
        ]
      }
    ],
    []
  );

  useEffect(() => {
    (async () => {
      const result = await axios("http://localhost:8080/api/employees");
      setData(result.data);
    })();
  }, []);

  const selectFile = (event) => {
    setSelectedFiles(event.target.files);
  };

  const upload = () => {
    let currentFile = selectedFiles[0];

    setProgress(0);
    setCurrentFile(currentFile);

    uploadToServer(currentFile, (event) => {
      setProgress(Math.round((100 * event.loaded) / event.total));
    })
      .then(async (response) => {
        setMessage(response.data.message);
        const result = await axios("http://localhost:8080/api/employees");
        setData(result.data);
      })
      .catch(() => {
        setProgress(0);
        setMessage("Could not upload the file!");
        setCurrentFile(undefined);
      });

    setSelectedFiles(undefined);
  };

  return (
    <div className="App">
      <div>
      {currentFile && (
        <div className="progress">
          <div
            className="progress-bar progress-bar-info progress-bar-striped"
            role="progressbar"
            aria-valuenow={progress}
            aria-valuemin="0"
            aria-valuemax="100"
            style={{ width: progress + "%" }}
          >
            {progress}%
          </div>
        </div>
      )}

      <label className="btn btn-default">
        <input type="file" onChange={selectFile} />
      </label>

      <button
        className="btn btn-success"
        disabled={!selectedFiles}
        onClick={upload}
      >
        Upload
      </button>

      <div className="alert alert-light" role="alert">
        {message}
      </div>
    </div>
      <Table columns={columns} data={data} />
    </div>
  );
}

export default App;
