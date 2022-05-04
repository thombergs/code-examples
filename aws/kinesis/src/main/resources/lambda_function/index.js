console.log('Loading function');

const validateRecord = (recordElement)=>{
    // record is considered valid if contains status field
    return recordElement.includes("status")
}

exports.handler = async (event, context) => {
    /* Process the list of records and transform them */
    const output = event.records.map((record)=>{
        const decodedData = Buffer.from(record.data, "base64").toString("utf-8")
        let isValidRecord = validateRecord(decodedData)

        if(isValidRecord){
            let parsedRecord = JSON.parse(decodedData)
            // read fields from parsed JSON for some more processing
            const outputRecord = `status::${parsedRecord.status}`
            return {
                recordId: record.recordId,
                result: 'Ok',
                // payload is encoded back to base64 before returning the result
                data:  Buffer.from(outputRecord, "utf-8").toString("base64")
            }

        }else{
            return {
                recordId: record.recordId,
                result: 'dropped',
                data: record.data // payload is kept intact,
            }
        }
    })
};
