import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TwitterMessage } from './twitter-message.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TwitterMessageService {

    private resourceUrl = 'api/twitter-messages';
    private resourceSearchUrl = 'api/_search/twitter-messages';

    constructor(private http: Http) { }

    create(twitterMessage: TwitterMessage): Observable<TwitterMessage> {
        const copy = this.convert(twitterMessage);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(twitterMessage: TwitterMessage): Observable<TwitterMessage> {
        const copy = this.convert(twitterMessage);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<TwitterMessage> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(twitterMessage: TwitterMessage): TwitterMessage {
        const copy: TwitterMessage = Object.assign({}, twitterMessage);
        return copy;
    }
}
