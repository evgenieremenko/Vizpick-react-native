import React, {Component} from 'react';
import {Platform, View, NativeModules, Dimensions, Text} from 'react-native';
import { Container } from 'native-base';
import Camera, 
{
  Aspect,
  CaptureQuality,
  TorchMode,
  RotateMode,
  takePicture,
} from './VispickCamera'
import Aruco from './ArucoNativeModule'
const ArucoModule = NativeModules.Aruco;


export default class App extends Component {

  state = {
    showOptions: false,
    plate: 'Point at a plate',
    confidence: '',
    error: null,
    showCamera: false,
    // Camera options
    camera: {
      aspect: Aspect.fill,
    },
    captureQuality: CaptureQuality.AVCaptureSessionPreset1280x720,
    aspect: CaptureQuality.stretch,
    zoom: 0,
    rotateMode: false,
    torchMode: false,
    showPlateOutline: true,
    plateOutlineColor: '#ff0000',
    country: 'eu',
    touchToFocus: false,

  }
componentWillMount(){
  Aruco.emitter.addListener('CAMERA_FRAME_EVENT', ({markers})=>{
    this.setState(()=>({markerList: markers}))
  });
  ArucoModule.setRNScreenDimensions(
    Dimensions.get("window").width,
    Dimensions.get("window").height
);
}

  render() {
    const {
        captureQuality,
        aspect,
        rotateMode,
    } = this.state;
    return (
      <Container>
        <Camera 
          style={{flex: 1}}
          aspect={aspect}
          captureQuality={captureQuality}
          rotateMode={this.state.rotateMode ? RotateMode.on : RotateMode.off}
        />

        {(this.state.markerList===[] || this.state.markerList === undefined || this.state.markerList ===null) ? null : this.identifyMarkers() }
      </Container>
    );
  }
  

  identifyMarkers = function(){
    return this.state.markerList.map(marker=>{
      return(
          // <View style={this.positionStyle(marker)}>
            <Text style={this.positionStyle(marker)}>
              {"[" + marker.rnx + ", " + marker.rny + "]"} 
            </Text>
          // </View>
      )
    })
  }

  positionStyle = function(marker){
    return {
      position: 'absolute',
      top: marker.rny,
      left: marker.rnx,
      alignItems: 'center',
    }
  }
}

