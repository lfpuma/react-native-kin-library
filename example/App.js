import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';

import KinLibrary from 'react-native-kin-library';

export default class App extends React.Component {
  onPress = () => {
    KinLibrary.startSDK();
  };

  render = () => (
    <View style={styles.container}>
      <TouchableOpacity onPress={this.onPress}>
        <Text style={styles.text}>Click here to test wrapper</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
  },
  text: {
    fontWeight: 'bold',
    fontSize: 24,
    marginTop: 100,
  },
});
