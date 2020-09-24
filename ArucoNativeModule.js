import {NativeModules, NativeEventEmitter} from 'react-native';

const {Aruco} = NativeModules;

const ArucoEmitter = new NativeEventEmitter(Aruco);

export default {
    emitter: ArucoEmitter
}