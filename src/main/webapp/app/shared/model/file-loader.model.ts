export interface IFileLoader {
    file?: any;
    fileContentType?: any;
}

export class FileLoader implements IFileLoader {
    constructor(public file?: any, public fileContentType?: any) {
    }
}
